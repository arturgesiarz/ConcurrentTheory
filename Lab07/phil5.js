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

Philosopher.prototype.startConductor = function (count, conductor) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  // zaimplementuj rozwiazanie z kelnerem
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
  const eat = (iteration) => {
    if (iteration >= count) {
      console.log(`Filozof ${id} zakonczyl jedzenie!`);
      return;
    }

    console.log(
      `Filozof ${id} prosi kelnera o zgodę na podniesienie widelców.`
    );

    conductor.requestPermission(() => {
      console.log(`Kelner wyraził zgodę dla filozofa ${id}.`);

      forks[f1].acquire(() => {
        console.log(`Filozof ${id} podniósł widelec ${f1}.`);

        forks[f2].acquire(() => {
          console.log(`Filozof ${id} podniósł widelec ${f2}`);

          console.log(`Filozof ${id} je.`);
          setTimeout(() => {
            forks[f1].release();
            console.log(`Filozof ${id} zwolnił widelec ${f1}.`);

            forks[f2].release();
            console.log(`Filozof ${id} zwolnił widelec ${f2}.`);

            conductor.releaseForks();

            eat(iteration + 1);
          }, 1000);
        });
      });
    });
  };

  eat(0);
};

// Conductor class
var Conductor = function (forksNumber) {
  this.waitingPhilosphers = [];
  this.avaiableForks = forksNumber;
  return this;
};

Conductor.prototype.requestPermission = function (cb) {
  var avaiableForks = this.avaiableForks;

  if (avaiableForks >= 2) {
    avaiableForks -= 2;
    this.avaiableForks = avaiableForks;
    cb();
  } else {
    this.waitingPhilosphers.push(cb);
  }
};

Conductor.prototype.releaseForks = function () {
  var waitingPhilosphers = this.waitingPhilosphers;
  var avaiableForks = this.avaiableForks;
  avaiableForks += 2;

  while (avaiableForks >= 2 && waitingPhilosphers.length > 0) {
    avaiableForks -= 2;
    this.avaiableForks = avaiableForks;
    const cb = waitingPhilosphers.shift();
    cb();
  }
};

// TEST - Functions
const test_phil_naive = function (
  philosophersNumber = 5,
  forksNumber = 5,
  eatingCounter = 10
) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < forksNumber; i++) {
    forks.push(new Fork());
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startNaive(eatingCounter);
  }
};

const test_phil_asym = function (
  philosophersNumber = 5,
  forksNumber = 5,
  eatingCounter = 10
) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < forksNumber; i++) {
    forks.push(new Fork());
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startAsym(eatingCounter);
  }
};

const test_phil_conductor = function (
  philosophersNumber = 5,
  forksNumber = 5,
  eatingCounter = 10
) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < forksNumber; i++) {
    forks.push(new Fork());
  }
  var conductor = new Conductor(forks.length);

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startConductor(eatingCounter, conductor);
  }
};

// test_phil_naive(5, 5, 1);
// test_phil_asym(5, 5, 1);
test_phil_conductor(5, 5, 3);
