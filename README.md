# Bookstore
This application was generated using JHipster 4.2.0, you can find documentation and help at [https://jhipster.github.io/documentation-archive/v4.2.0](https://jhipster.github.io/documentation-archive/v4.2.0).

## Development

For modify and build this application you have to install:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

   Node will install node and npm applications

   We use Bower for manage CSS and JavaScript dependencies, so we need to install with the command

    npm install -g bower

2. Postgresql Database
   For development, you have to install Postgresql and create a database called "Bookstore"

## Building for production

To optimize the Bookstore application for production, run:

    mvn -Pprod clean package

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

    java -jar target/*.war

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Credits

[JHipster Homepage]: https://jhipster.github.io

[Node.js]: https://nodejs.org/

[Bower]: http://bower.io/
  For Manage CSS and JavaScript

[Gulp]: http://gulpjs.com/
For Debug JavaScript, CSS, HTML.
  Bundling and minifying libraries and stylesheets.
  Refreshing your browser when you save a file.
  Quickly running unit tests
  Running code analysis
  Copying modified files to an output directory

Authors
 Gerardo LARREINEGABE
 Gaurang RATNAPARKHI
 Patricio PERPETUA
