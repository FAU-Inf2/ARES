RECOMMENDATION 0

class AresInput {

  example() {
    Foo foo = Library.getObject();
    this.init(getVerbose());
    updateValue();
    for (c = 0; c < 10; c++)
      System.out.println(c);
    if (foo != null) {
      foo.someMethod(99);
    }
    this.shutdown();
    return foo;
  }

}


RECOMMENDATION 1

class AresInput {

  example() {
    Foo foo = Library.getObject();
    this.init(getVerbose());
    updateValue();
    for (c = 0; c < 10; c++)
      System.out.println(c);
    if (foo != null) {
      foo.someMethod(99);
    }
    System.out.print(foo);
    this.shutdown();
    return foo;
  }

}


RECOMMENDATION 2

class AresInput {

  example() {
    Foo foo = Library.getObject();
    this.init(getVerbose());
    updateValue();
    for (c = 0; c < 10; c++)
      System.out.println(c);
    if (foo != null) {
      foo.someMethod(99);
    }
    this.print(foo);
    this.shutdown();
    return foo;
  }

}


