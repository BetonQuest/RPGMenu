#!/bin/bash

if [ -z "$2" ]; then
    echo -e "WARNING!!\nYou need to pass the WEBHOOK_URL environment variable as the second argument to this script.\nFor details & guide, visit: https://github.com/k3rn31p4nic/travis-ci-discord-webhook" && exit
fi

if [ -z "$TRAVIS_TAG" ]; then

    echo -e "[Webhook]: Sending webhook to Discord...\\n";

    case $1 in
        "success" )
            EMBED_COLOR=3066993
            STATUS_MESSAGE="Passed"
            STATUS_ICON="https://raw.githubusercontent.com/travis-ci/travis-api/master/public/images/result/passing.png"
            ;;

        "failure" )
            EMBED_COLOR=15158332
            STATUS_MESSAGE="Failed"
            STATUS_ICON="https://raw.githubusercontent.com/travis-ci/travis-api/master/public/images/result/failing.png"
            ;;

        * )
            EMBED_COLOR=0
            STATUS_MESSAGE="Status Unknown"
            STATUS_ICON="https://raw.githubusercontent.com/travis-ci/travis-api/master/public/images/result/unknown.png"
            ;;
    esac

    AUTHOR_NAME="$(git log -1 "$TRAVIS_COMMIT" --pretty="%aN")"
    COMMITTER_NAME="$(git log -1 "$TRAVIS_COMMIT" --pretty="%cN")"
    COMMIT_SUBJECT="$(git log -1 "$TRAVIS_COMMIT" --pretty="%s")"
    COMMIT_MESSAGE="$(git log -1 "$TRAVIS_COMMIT" --pretty="%b")"

    if [ "$AUTHOR_NAME" == "$COMMITTER_NAME" ]; then
        CREDITS="$AUTHOR_NAME authored & committed"
    else
        CREDITS="$AUTHOR_NAME authored & $COMMITTER_NAME committed"
    fi

    if [[ $TRAVIS_PULL_REQUEST != false ]]; then
        URL="https://github.com/$TRAVIS_REPO_SLUG/pull/$TRAVIS_PULL_REQUEST"
    else
        URL=""
    fi

    TIMESTAMP=$(date --utc +%FT%TZ)
    WEBHOOK_DATA='{
      "username": "",
      "avatar_url": "",
      "embeds": [ {
        "color": '$EMBED_COLOR',
        "author": {
          "name": "Job #'"$TRAVIS_JOB_NUMBER"' (Build #'"$TRAVIS_BUILD_NUMBER"') '"$STATUS_MESSAGE"' - '"$TRAVIS_REPO_SLUG"'",
          "url": "https://travis-ci.org/'"$TRAVIS_REPO_SLUG"'/builds/'"$TRAVIS_BUILD_ID"'"
        },
        "title": "'"$COMMIT_SUBJECT"'",
        "url": "'"$URL"'",
        "description": "'"${COMMIT_MESSAGE//$'\n'/ }"\\n\\n"$CREDITS"'",
        "thumbnail": {
          "url": "'"$STATUS_ICON"'"
        },
        "fields": [
          {
            "name": "Commit",
            "value": "'"[\`${TRAVIS_COMMIT:0:7}\`](https://github.com/$TRAVIS_REPO_SLUG/commit/$TRAVIS_COMMIT)"'",
            "inline": true
          },
          {
            "name": "Branch/Tag",
            "value": "'"[\`$TRAVIS_BRANCH\`](https://github.com/$TRAVIS_REPO_SLUG/tree/$TRAVIS_BRANCH)"'",
            "inline": true
          }
        ],
        "timestamp": "'"$TIMESTAMP"'"
      } ]
    }'

    (curl --fail --progress-bar -A "TravisCI-Webhook" -H Content-Type:application/json -H X-Author:k3rn31p4nic#8383 -d "$WEBHOOK_DATA" "$2" \
      && echo -e "\\n[Webhook]: Successfully sent the webhook.") || echo -e "\\n[Webhook]: Unable to send webhook:\\n\\nWEBHHOK_DATA:\\n$WEBHHOK_DATA"

fi