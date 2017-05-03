# Dataservice

## Overview

Framework library for building GWT applications using the MVP design pattern. On one hand it provides functionality separation, on other, easy data transfer from server to client and vice versa using GWT RPC service


## Building From Source

[Maven](http://maven.apache.org) is used to build and deploy.

Run build jar:

```sh
$ mvn package
```

The resulting jar files are in `target/`.

Generate the documentation:

```sh
$ mvn javadoc:javadoc
```

The resulting HTML files are in `target/site/apidocs/`.


## Using From Maven

Any Maven based project can use it directly by adding the appropriate entries to the
`dependencies` section of its `pom.xml` file:

```xml
<dependencies>
  <dependency>
    <groupId>org.homedns.mkh</groupId>
    <artifactId>dataservice</artifactId>
    <version>1.0.7</version>
  </dependency>
</dependencies>
```


## Using From Binaries

Packaged jars can be downloaded directly from the [Releases page](https://github.com/khomisha/dataseervice/releases).


## Contact

* mkhodonov@gmail.com

## Donate

[![Donate](https://www.paypalobjects.com/en_US/i/btn/btn_donate_LG.gif)](https://load.payoneer.com/LoadToPage.aspx)

## License

Apache License, Version 2.0
Copyright (c) 2012-2017 Mikhail Khodonov.
It is free software and may be redistributed under the terms specified
in the LICENSE and NOTICE files.


