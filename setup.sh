#!/bin/bash

handle_error() {
  local exit_code="$?"
  exit "$exit_code"
}

trap 'handle_error' ERR

echo '
       /$$                                            /$$$$$$$$                           /$$
      | $$                                           |__  $$__/                          | $$
      | $$        /$$$$$$   /$$$$$$   /$$$$$$  /$$$$$$$ | $$  /$$$$$$  /$$$$$$   /$$$$$$$| $$   /$$
      | $$       /$$__  $$ |____  $$ /$$__  $$| $$__  $$| $$ /$$__  $$|____  $$ /$$_____/| $$  /$$/
      | $$      | $$$$$$$$  /$$$$$$$| $$  \__/| $$  \ $$| $$| $$  \__/ /$$$$$$$| $$      | $$$$$$/
      | $$      | $$_____/ /$$__  $$| $$      | $$  | $$| $$| $$      /$$__  $$| $$      | $$_  $$
      | $$$$$$$$|  $$$$$$$|  $$$$$$$| $$      | $$  | $$| $$| $$     |  $$$$$$$|  $$$$$$$| $$ \  $$
      |________/ \_______/ \_______/|__/      |__/  |__/|__/|__/      \_______/ \_______/|__/  \__/

'

echo "Hi there! This script will set up your development environment for learnTrack."
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

echo "⚙️ Installing Playwright browsers"
yarn playwright install --with-deps
echo

popd > /dev/null || exit

echo "⚙️ Configuring testcontainers for reuse"
file_path=~/.testcontainer.properties
line_to_add="testcontainers.reuse.enable=true"
if [ ! -e "$file_path" ]; then
  touch "$file_path"
fi
if ! grep -qF "$line_to_add" "$file_path"; then
  echo "$line_to_add" >>"$file_path"
  echo " 👉 testcontainers will now be reused"
else
  echo " 👉 testcontainers reuse already configured"
fi

echo "🎉 Setup complete, happy hacking!"
echo
