#!/bin/bash

handle_error() {
  local exit_code="$?"
  exit "$exit_code"
}

trap 'handle_error' ERR

echo '

     /$$                                         /$$   /$$ /$$$$$$$
    | $$                                        | $$  | $$| $$__  $$
    | $$  /$$$$$$   /$$$$$$   /$$$$$$  /$$$$$$$ | $$  | $$| $$  \ $$
    | $$ /$$__  $$ |____  $$ /$$__  $$| $$__  $$| $$  | $$| $$$$$$$/
    | $$| $$$$$$$$  /$$$$$$$| $$  \__/| $$  \ $$| $$  | $$| $$____/
    | $$| $$_____/ /$$__  $$| $$      | $$  | $$| $$  | $$| $$
    | $$|  $$$$$$$|  $$$$$$$| $$      | $$  | $$|  $$$$$$/| $$
    |__/ \_______/ \_______/|__/      |__/  |__/ \______/ |__/

'

echo "Hi there! This script will set up your development environment for learnUP."
read -p "ℹ️ Press Enter to continue..."
echo

echo "️️⚙️ Configuring git hooks path (tools/git-hooks)"
git config core.hooksPath tools/git-hooks
echo

echo "⚙️ Installing Java SDK"
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk env install
echo

pushd ./frontend > /dev/null || exit

echo "⚙️ Installing Node.js"
source "$HOME/.nvm/nvm.sh"
nvm install
echo

echo "⚙️ Enabling Corepack (for Yarn)"
corepack enable
echo

echo "⚙️ Installing Yarn dependencies"
yarn install
echo

popd > /dev/null || exit

echo "🎉 Setup complete, happy hacking!"
echo
