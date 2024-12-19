// Fork class
var Fork = function () {
  this.state = 0;
  return this;
};

Fork.prototype.acquire = function (cb) {
  // zaimplementuj funkcje acquire, tak by korzystala z algorytmu BEB
  // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
  // 1. przed pierwsza proba podniesienia widelca Filozof odczekuje 1ms
  // 2. gdy proba jest nieudana, zwieksza czas oczekiwania dwukrotnie
  //    i ponawia probe itd.
  let delay = 1;

  const attemptAcquire = () => {
    if (this.state === 0) {
      this.state = 1;
      cb();
    } else {
      delay *= 2;
      setTimeout(attemptAcquire, delay);
    }
  };

  setTimeout(attemptAcquire, delay);
};

Fork.prototype.release = function () {
  this.state = 0;
};

// Philosopher class
var Philosopher = function (id, forks) {
  this.id = id;
  this.forks = forks;
  this.f1 = id % forks.length;
  this.f2 = (id + 1) % forks.length;
  return this;
};

Philosopher.prototype.startNaive = function (count) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  // zaimplementuj rozwiazanie naiwne
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
  const eat = (iteration) => {
    if (iteration >= count) {
      console.log(`Filozof ${id} zakonczyl jedzenie!`);
      return;
    }
    console.log(`Filozof ${id} próbuje podnieśc widelce ${f1} i ${f2}.`);

    forks[f1].acquire(() => {
      console.log(`Filozof ${id} podniósł widelec ${f1}.`);

      forks[f2].acquire(() => {
        console.log(`Filozof ${id} podniósł widelec ${f2}`);

        console.log(`Filozof ${id} je.`);
        setTimeout(() => {
          forks[f1].release();
          console.log(`Filozof ${id} zwolnil widelec ${f1}.`);

          forks[f2].release();
          console.log(`Filozof ${id} zwolnil widelec ${f2}.`);

          eat(iteration + 1);
        }, 1000);
      });
    });
  };

  eat(0);
};

Philosopher.prototype.startAsym = function (count) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  // zaimplementuj rozwiazanie asymetryczne
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
  const eat = (iteration) => {
    if (iteration >= count) {
      console.log(`Filozof ${id} zakonczyl jedzenie!`);
      return;
    }

    console.log(`Filozof ${id} probuje podniesc widelce.`);

    if (id % 2 === 0) {
      forks[f2].acquire(() => {
        console.log(`Filozof ${id} podniósł widelec ${f2}.`);

        forks[f1].acquire(() => {
          console.log(`Filozof ${id} podniósł widelec ${f1}`);

          console.log(`Filozof ${id} je.`);
          setTimeout(() => {
            forks[f2].release();
            console.log(`Filozof ${id} zwolnil widelec ${f2}.`);

            forks[f1].release();
            console.log(`Filozof ${id} zwolnil widelec ${f1}.`);

            eat(iteration + 1);
          }, 1000);
        });
      });
    } else {
      forks[f1].acquire(() => {
        console.log(`Filozof ${id} podniósł widelec ${f1}.`);

        forks[f2].acquire(() => {
          console.log(`Filozof ${id} podniósł widelec ${f2}`);

          console.log(`Filozof ${id} je.`);
          setTimeout(() => {
            forks[f1].release();
            console.log(`Filozof ${id} zwolnil widelec ${f1}.`);

            forks[f2].release();
            console.log(`Filozof ${id} zwolnil widelec ${f2}.`);

            eat(iteration + 1);
          }, 1000);
        });
      });
    }
  };

  eat(0);
};

Philosopher.prototype.startConductor = function (count) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  // zaimplementuj rozwiazanie z kelnerem
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
};

// Test
var N = 5;
var forks = [];
var philosophers = [];

for (var i = 0; i < N; i++) {
  forks.push(new Fork());
}

for (var i = 0; i < N; i++) {
  philosophers.push(new Philosopher(i, forks));
}

for (var i = 0; i < N; i++) {
  philosophers[i].startNaive(10);
}
