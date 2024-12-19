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
};

Philosopher.prototype.startAsym = function (count) {
  var forks = this.forks,
    f1 = this.f1,
    f2 = this.f2,
    id = this.id;

  // zaimplementuj rozwiazanie asymetryczne
  // kazdy filozof powinien 'count' razy wykonywac cykl
  // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
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
