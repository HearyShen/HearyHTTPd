# HearyHTTPd
An experimental Reactor HTTP server written in Java.

## Introduction

[HearyHTTPd(hhttpd)](https://github.com/HearyShen/HearyHTTPd) mainly consists of a MainReactor thread quickly accepting HTTP requests and a SubReactor thread handling accepted readable requests with a thread pool.

For high efficiency, hhttpd applies NIO and non-blocking selecting method to save waiting time and mapping file into memory to speed up I/O.

hhttpd supports HTTP GET, HEAD and POST method.

hhttpd can handle requests for static resources and dynamic CGI.

The structure can be described as below:

- MainReactor (single thread)
- SubReactor (single thread)
  - HttpWorker (Runnable in thread pool)
    - GetProcesser
      - TextResponser
      - BinaryResponser
      - NotFoundResponser
      - etc.
    - PostProcesser
      - BytesResponser
    - etc.

## Run

hhttpd is developed with JDK11.

hhttpd can compiled and launched with the following commands:

```bash
cd src
javac hhttpd/launcher/Launcher.java
java hhttpd.launcher.Launcher
```

