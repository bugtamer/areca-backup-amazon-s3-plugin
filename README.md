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

`ant [-Dareca.dir=<Areca installation directory>] [fetch-areca.jar] [deploy-to-areca] [-Ddebug=on]`

- `-Dareca.dir=/path-to/areca-backup` sets `areca.dir` to `/path-to/areca-backup` that points to Areca folder.
- `fetch-areca.jar` copies `${areca.dir}/libs/areca.jar` to `areca-backup-amazon-s3-plugin/libs/areca.jar`.
- `deploy-to-areca` uncompress `areca-backup-amazon-s3-plugin/releases/areca-plugin-as3-x.y.z.zip` into `${areca.dir}/plugins`.
- `-Ddebug=on` allows you to enable debug mode.

Examples:

- `ant` or `ant compile` to compile and bundle the plugin.
- `ant fetch-areca.jar deploy-to-areca -Dareca.dir=/path-to/areca-backup`
- `ant -Dareca.dir=/path-to/areca-backup fetch-areca.jar`
- `ant -Dareca.dir=/path-to/areca-backup deploy-to-areca`
- `ant -Ddebug=on`


## License

[GNU General Public License version 2.0 (GPLv2)](/LICENSE)


## Credit

- (C) 2009-2015 [Robert Bernhardt](https://sourceforge.net/u/rbernhardt/profile/)
- [Source Code](https://sourceforge.net/projects/areca-amazon-s3/)