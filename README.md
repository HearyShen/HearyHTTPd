# HearyHTTPd
An experimental Reactor HTTP server written in Java.

## Introduction

[HearyHTTPd(hhttpd)](https://github.com/HearyShen/HearyHTTPd) mainly consists of a MainReactor thread quickly accepting HTTP requests and a SubReactor thread handling accepted readable requests with a thread pool.

For high efficiency, hhttpd applies NIO and non-blocking selecting method to save waiting time and mapping file into memory to speed up I/O.

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
    - etc.

## Usage

hhttpd can be launched with simply the following command after compiling:

```bash
java -classpath D:\Codes\Java\HearyHTTPd\out\production\HearyHTTPd hhttpd.launcher.Launcher
```

