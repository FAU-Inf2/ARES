class AresInput {

  example() {
    d = 1.0;
    this.init(true);
    this.shutdown();
    updateValue();
    j = 0;
    while (j < 10) {
      String tmp = "bar";
      this.execute(tmp);
      j++;
    }
    foo.someMethod(23);
  }

}
