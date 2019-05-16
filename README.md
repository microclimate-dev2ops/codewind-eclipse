# Codewind for Eclipse

[![License](https://img.shields.io/badge/License-EPL%202.0-red.svg)](https://www.eclipse.org/legal/epl-2.0/)

## Contributing

We welcome [issues](https://github.com/microclimate-dev2ops/codewind-eclipse/issues) and contributions. For more information, see [CONTRIBUTING.md](https://github.com/microclimate-dev2ops/codewind-eclipse/tree/master/CONTRIBUTING.md).

### Enabling Debug Logs

1. Create a file called `.options` in your Eclipse install directory (the same directory with the `eclipse` executable) with the following content:

`com.ibm.microclimate.core/debug/info=true`

2. Launch eclipse with the `-debug` flag.
3. The logs are written to the Eclipse workspace directory, to `.metadata/.log`.

## Building

1. Clone the repository to your system.

    ```git clone https://github.com/microclimate-dev2ops/codewind-eclipse```

2. [Optional] Copy 'codewind-eclipse' folder to 'build' to get a test build. This will keep your source folder intact.
3. Run a gradle build.

    ```cd build/dev```

    ```./gradlew```

4. Test the driver built from Step. 3

    ```build/dev/ant_build/artifacts/codewind-[Version].vYYYYMMDD_hhmm.zip```

## License

[EPL 2.0](https://github.com/microclimate-dev2ops/codewind-eclipse/tree/master/LICENSE)

## Dependencies

| Dependency | License |
| ---------- | ------- |
| [socket.io-client-1.0.0.jar](https://mvnrepository.com/artifact/io.socket/socket.io-client/1.0.0) | [MIT](http://opensource.org/licenses/mit-license) |
| [engine.io-client-1.0.0.jar](https://mvnrepository.com/artifact/io.socket/engine.io-client/1.0.0) | [MIT](https://opensource.org/licenses/mit-license) |
| [json-20090211.jar](https://mvnrepository.com/artifact/org.json/json/20090211) | [The JSON License](http://www.json.org/license.html) |
| [okhttp-3.8.1.jar](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp/3.8.1) | [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) |
| [okio-1.13.0.jar](https://mvnrepository.com/artifact/com.squareup.okio/okio/1.13.0) | [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt) |
