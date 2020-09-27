GITLAB_TOKEN_FILE=$1
GITLAB_TOKEN=$(cat ${GITLAB_TOKEN_FILE:-.gitlab.token})

REF=master

curl -X POST \
     -F token=$GITLAB_TOKEN \
     -F ref=$REF \
     https://gitlab.com/api/v4/projects/15188575/trigger/pipeline