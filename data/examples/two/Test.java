class AresInput {

  example() {
    Foo foo = Library.getObject();
    this.init(getVerbose());
    this.shutdown();
    updateValue();
    c = 0;
    while (c < 10) {
      System.out.println(c);
      c++;
    }
    foo.someMethod(99);
    return foo;
  }

}
