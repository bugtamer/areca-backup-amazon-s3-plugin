# Amazon S3 plugin

Amazon S3 plugin to backup files with Areca Backup to an Amazon S3 bucket.


## Development environment setup

- [`Git`](https://git-scm.com/)
- [`OpenJDK`](https://adoptium.net/temurin/releases/)
- [`Apache Ant`](https://ant.apache.org/)
- [`Apache Ivy`](https://ant.apache.org/ivy/)
  `Apache Ant` will download `Apache Ivy` if it is not available on your system.
- [`classpath` configuration](.vscode/settings.json) for [Visual Studio Code](https://code.visualstudio.com/).
- Add `areca.jar` to `areca-backup-amazon-s3-plugin/libs` directory from
  - [Bugtamer's releases](https://sourceforge.net/projects/areca-backup/files/areca-stable/) for the latest versions of Areca.
  - [Areca Backup repo](https://github.com/bugtamer/areca-backup) to build your custom `areca.jar`.
  - [aventin's releases](https://sourceforge.net/projects/areca/files/areca-stable/) (Olivier Petrucci) for the original versions of Areca.

### Relevant files

- [`build.xml`](/build.xml)
  Proyect commands to build a release
- [`dependency-manager.xml`](/dependency-manager.xml)
  Ivy specific commands
- [`ivy.xml`](/ivy.xml)
  Project dependencies
- [`ivysettings.xml`](/ivysettings.xml)
  Ivy configuration


## Build a release

Examples:

- `ant windows-x86-64`
  <br>
  to compile and bundle the plugin but do not fetch `areca.jar`.
- `ant linux-x86-64 -Dareca.dir=/path-to/areca-backup`
  <br>
  to compile, bundle, fetch `areca.jar` (from `/path-to/areca-backup/libs/areca.jar`) and, deploy the plugin to directory `/path-to/areca-backup/plugins`.
- `ant linux-x86-64 -Dareca.dir=/path-to/areca-backup -Ddebug=on`
  <br>
  `-Ddebug=on` allows you to enable debug mode.


## License

[GNU General Public License version 2.0 (GPLv2)](/LICENSE)


## Credit

- (C) 2009-2015 [Robert Bernhardt](https://sourceforge.net/u/rbernhardt/profile/)
- [Source Code](https://sourceforge.net/projects/areca-amazon-s3/)