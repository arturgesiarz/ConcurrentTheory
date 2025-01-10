// Save to file
const fs = require("fs");
const path = require("path");

const appendToCSV = function (fileName, philosophersNumber, start) {
  const filePath = path.join(__dirname, fileName);
  const timestamp = new Date().getTime() - start;
  const line = `${philosophersNumber},${timestamp}\n`;

  fs.appendFile(filePath, line, (err) => {
    if (err) {
      console.error("Błąd podczas zapisu do pliku:", err);
    }
  });
};

// Fork class
var Fork = function () {
  this.state = 0;
  return this;
};

Fork.prototype.acquire = function (cb, time = 1) {
  // zaimplementuj funkcje acquire, tak by korzystala z algorytmu BEB
  // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
  // 1. przed pierwsza proba podniesienia widelca Filozof odczekuje 1ms
  // 2. gdy proba jest nieudana, zwieksza czas oczekiwania dwukrotnie
  //    i ponawia probe itd.
  setTimeout(() => {
    if (this.state == 0) {
      this.state = 1;
      cb();
    } else {
      console.log(`Czekam ${time * 2}`);
      this.acquire(cb, time * 2);
    }
  }, time);
};

// Acquire for conductor
Fork.prototype.acquireLeft = function (cb, philosophersNumber) {
  var self = this;

  var check = function (time) {
    if (self.state === 0) {
      if (acquiredForks >= philosophersNumber - 1) {
        setTimeout(check, 1, 1);
      } else {
        self.state = 1;
        cb();
      }
    } else {
      setTimeout(check, time, time * 2);
    }
  };

  setTimeout(check, 1, 1);
};

Fork.prototype.acquireRight = function (cb) {
  var self = this;
  var check = function (time) {
    if (self.state === 0) {
      self.state = 1;
      cb();
    } else {
      setTimeout(check, time, time * 2);
    }
  };
  setTimeout(check, 1, 1);
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
          forks[f2].release();
          console.log(`Filozof ${id} zwolnil widelec ${f1}.`);

          forks[f1].release();
          console.log(`Filozof ${id} zwolnil widelec ${f2}.`);

          eat(iteration + 1);
        }, 100);
      });
    });
  };

  eat(0);
};

Philosopher.prototype.startAsym = function (count, philosophersNumber) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  var left, right;

  if (id % 2 === 0) {
    left = this.f1;
    right = this.f2;
  } else {
    left = this.f2;
    right = this.f1;
  }

  // zaimplementuj rozwiazanie asymetryczne
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
  eat = (iteration) => {
    if (iteration >= count) {
      console.log(`Filozof ${id} zakonczyl jedzenie!`);
      return;
    }

    console.log(`Filozof ${id} probuje podniesc widelce.`);
    var start = new Date().getTime();
    forks[left].acquire(() => {
      console.log(`Filozof ${id} podniósł widelec ${left}.`);

      forks[right].acquire(() => {
        // appendToCSV("phil_asym.csv", philosophersNumber, start);
        console.log(`Filozof ${id} podniósł widelec ${right}`);

        console.log(`Filozof ${id} je.`);
        setTimeout(() => {
          forks[right].release();
          console.log(`Filozof ${id} zwolnil widelec ${right}.`);

          forks[left].release();
          console.log(`Filozof ${id} zwolnil widelec ${left}.`);

          eat(iteration + 1);
        }, 100);
      });
    });
  };

  eat(0);
};

Philosopher.prototype.startConductor = function (count, philosophersNumber) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  eat = function (iteration) {
    if (iteration >= count) {
      console.log(`Filozof ${id} zakonczyl jedzenie!`);
      return;
    }

    start = new Date();
    console.log(`Filozof ${id} czeka na pozwolenie lokaja.`);

    forks[f1].acquireLeft(() => {
      acquiredForks += 1;

      forks[f2].acquireRight(() => {
        acquiredForks += 1;
        console.log(`Filozof ${id} dostal pozwolenie lokaja.`);
        // appendToCSV("phil_conductor.csv", philosophersNumber, start);

        console.log(`Filozof ${id} je.`);
        setTimeout(() => {
          forks[f1].release();
          console.log(`Filozof ${id} zwolnil widelec ${f1}.`);

          forks[f2].release();
          console.log(`Filozof ${id} zwolnil widelec ${f2}.`);

          acquiredForks -= 2;
          eat(iteration + 1);
        }, 100);
      });
    }, philosophersNumber);
  };

  eat(0);
};

// TEST - Functions
const test_phil_naive = function (philosophersNumber = 5, eatingCounter = 10) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < philosophersNumber; i++) {
    forks.push(new Fork());
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startNaive(eatingCounter);
  }
};

const test_phil_asym = function (philosophersNumber = 5, eatingCounter = 10) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < philosophersNumber; i++) {
    forks.push(new Fork());
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startAsym(eatingCounter, philosophersNumber);
  }
};

const test_phil_conductor = function (
  philosophersNumber = 5,
  eatingCounter = 10
) {
  var forks = [];
  var philosophers = [];

  for (var i = 0; i < philosophersNumber; i++) {
    forks.push(new Fork());
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers.push(new Philosopher(i, forks));
  }

  for (var i = 0; i < philosophersNumber; i++) {
    philosophers[i].startConductor(eatingCounter, philosophersNumber);
  }
};

// Normal Test
test_phil_naive(5);
// test_phil_asym(5);
// test_phil_conductor(5);
