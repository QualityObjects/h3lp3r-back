export BUILD_FRONT=true
export BUILD_BACK=true
export TARGET=develop
export WORKSPACE=$(pwd)

if [ "$BUILD_FRONT" = "true" ]; then
  rm -rf h3lp3r-front 2> /dev/null
  echo Construimos front...
  git clone https://gitlab.com/qo-oss/h3lp3r/h3lp3r-front.git
  cd h3lp3r-front
  git checkout ${TARGET} 
  cd ..
  tar -cvz -f h3lp3r-front-sources.tgz --exclude="node_modules" --exclude="dist" --exclude=".git" h3lp3r-front/ 
  cd h3lp3r-front/etc/docker/
  mv "$WORKSPACE/h3lp3r-front-sources.tgz" .
  docker build -t h3lp3r-front -t qualityobjects/h3lp3r-front --rm -f ./Dockerfile -m 8GB .
fi


if [ "$BUILD_BACK" = "true" ]; then
  rm -rf h3lp3r-back 2> /dev/null
  echo Construimos back...
  cd "$WORKSPACE"
  git clone https://gitlab.com/qo-oss/h3lp3r/h3lp3r-back.git
  
  cd h3lp3r-back
  git checkout ${TARGET} 
  cd ..
  tar -cvz -f h3lp3r-back-sources.tgz --exclude="*.jar" --exclude="*.tgz" --exclude="target" --exclude=".git" h3lp3r-back/ 
  cd h3lp3r-back/etc/docker
  mv "$WORKSPACE/h3lp3r-back-sources.tgz" .
  docker build -t h3lp3r-back -t qualityobjects/h3lp3r-back --rm -f ./Dockerfile -m 4GB .
fi


