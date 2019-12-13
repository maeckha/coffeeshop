# Short Facts about the Build Pipeline:

## Respository

HTWG IN GitLab Server

## CI Server

HTWG IN Jenkins with MultiPipeline setup
- master branch deploys to staging -> production
- development branch deploys to staging
- all other branches are unit tested

## Staging Environment

- Spring executable jar in /opt/coffeeshop/ belongs to user ubuntu
- Spring profile ``staging``
- Server: BWCloud 193.196.52.139
- DB Server: 193.196.52.162
    * Database: coffeeshop_staging
    * User: coffeeshop_staging


## Staging Environment

- Spring executable jar in /opt/coffeeshop/ belongs to user ubuntu
- Spring profile ``production``
- Server: BWCloud 193.196.52.226
- DB Server: 193.196.52.162
    * Database: coffeeshop
    * User: coffeeshop