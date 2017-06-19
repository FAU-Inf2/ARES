class AresInput {

  example() {
    d = 1.0;
    this.init(true);
    updateValue();
    for (j = 0; j < 10; j++) {
      String tmp = "bar";
      this.execute(tmp);
    }
    if (foo != null) {
      foo.someMethod(23);
    }
    this.print(foo);
    this.shutdown();
  }

}
