#!/bin/sh
# 一键推送到 GitHub
#
# 用法：
#   GITHUB_TOKEN=ghp_xxxxxxxx ./push.sh
#
# 可选：
#   REPO_NAME=xiuxian-game  REPO_OWNER=cpu888cpu
#
# Token 需要 repo 权限（classic）或 Contents: Read and write（fine-grained）

set -e

if [ -z "$GITHUB_TOKEN" ]; then
  echo "缺少 GITHUB_TOKEN 环境变量。"
  echo "示例：GITHUB_TOKEN=ghp_xxx ./push.sh"
  exit 1
fi

OWNER="${REPO_OWNER:-cpu888cpu}"
REPO="${REPO_NAME:-xiuxian-game}"
DESC="凡人问道 —— 凡人流文字修仙 | Kotlin·Compose + WebView 双版本"

echo "==> 1/3 创建仓库（已存在则跳过）"
curl -s -X POST "https://api.github.com/user/repos" \
  -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github+json" \
  -d "{\"name\":\"$REPO\",\"description\":\"$DESC\",\"private\":false,\"has_issues\":true,\"has_wiki\":false}" \
  > /dev/null || true

echo "==> 2/3 配置 remote"
git remote remove origin 2>/dev/null || true
git remote add origin "https://$GITHUB_TOKEN@github.com/$OWNER/$REPO.git"

echo "==> 3/3 推送"
git push -u origin main

echo ""
echo "完成：https://github.com/$OWNER/$REPO"