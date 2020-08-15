# HearyHTTPd
An experimental Reactor HTTP server written in Java.

## Introduction

[HearyHTTPd(hhttpd)](https://github.com/HearyShen/HearyHTTPd) mainly consists of a MainReactor thread quickly accepting HTTP requests and a SubReactor thread handling accepted readable requests with a thread pool.

For high efficiency, hhttpd applies NIO and non-blocking I/O multiplexing technology to save waiting time and mapping file into memory to speed up I/O.

hhttpd currently supports HTTP GET, HEAD and POST method.

hhttpd can handle requests for static resources with common MIME type and also dynamic CGI.

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

## Build and Run

hhttpd is developed with JDK11.

hhttpd can be compiled and launched with simply the following commands:

```bash
cd src
javac hhttpd/launcher/Launcher.java
java hhttpd.launcher.Launcher
```

## Benchmark

hhttpd has been tested with [Apache benchmark](https://httpd.apache.org/docs/2.4/programs/ab.html) and [WebBench](https://github.com/EZLippi/WebBench).

Take Apache benchmark as a more formal benchmark tool, test settings are 100k requests with 1000 concurrency:

```bash
ab -n 100000 -c 1000 http://localhost:8080/
```

On my dev desktop:
  - Specs:
    - Intel(R) Core(TM) i5-7500 CPU @ 3.40GHz
    - single channel 8GB DDR4 2400Mhz
  - Apache benchmark results show a **6400+ QPS** (`Requests per second:    6412.45 [#/sec] (mean)`)

On lab's computing server:

- Server1:
  - Specs:
    - Intel(R) Xeon(R) Gold 6132 CPU @ 2.60GHz × 2
    - 512G DDR4 2666Mhz with ECC (16 channels × 32G)
  - Apache benchmark results show a **21000+ QPS** (`Requests per second:    21041.56 [#/sec] (mean)`)

- Server2:
  - Specs
    - Intel(R) Xeon(R) Silver 4216 CPU @ 2.10GHz × 2
    - 128G DDR4 2666Mhz with ECC (4 channels × 32G)
  - Apache benchmark results show a **30000+** QPS(`Requests per second:    30338.50 [#/sec] (mean)`)

For more details, please refer to my [blog](https://heary.cn/posts/HTTP服务器压力测试/).

## Future Plan

- Develop a cache module for responses.
  - Cache valid and invalid, string and bytes responses in memory to avoid disk I/O;
  - Perhaps develop an LRU cache with `LinkedHashMap` and a daemon thread to remove out-of-date caches.
- Implementing more HTTP features can be an option.

