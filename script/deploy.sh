#!/bin/bash

# 设置颜色代码
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

# 设置状态图标
CHECK_MARK="${GREEN}✓${NC}"
CROSS_MARK="${RED}✗${NC}"
ARROW="${YELLOW}➜${NC}"

# 打印分隔线
print_separator() {
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
}

print_title() {
    print_separator
    echo -e "${BLUE}║${NC} ${CYAN}              Docker 部署工具 v1.0.0              ${BLUE}║${NC}"
    print_separator
    echo
}

# 打印步骤
print_step() {
    local step=$1
    local total=$2
    local message=$3
    echo -e "${WHITE}[$step/$total]${NC} ${YELLOW}$message${NC}"
}

# 打印成功信息
print_success() {
    echo -e "  ${CHECK_MARK} $1"
}

# 打印错误信息
print_error() {
    echo -e "  ${CROSS_MARK} $1"
}

# 打印信息
print_info() {
    echo -e "  ${ARROW} $1"
}

# 确保脚本在出错时退出
set -e

# 获取脚本所在目录并切换到项目根目录
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
cd "${SCRIPT_DIR}/.."
print_info "切换到项目根目录: $(pwd)"

# ==================== 配置变量区域 ====================

# 设置代理
export http_proxy=http://127.0.0.1:7890
export https_proxy=http://127.0.0.1:7890

# Docker镜像相关配置
DOCKER_IMAGE_NAME="yangbuyiya/tencent-send-article-mcp-server-app"
DOCKER_IMAGE_TAG="1.0.0"
DOCKER_PLATFORM="linux/amd64,linux/arm64"
DOCKERFILE_PATH="Dockerfile"

# 阿里云配置
ALIYUN_REGISTRY="registry.cn-hangzhou.aliyuncs.com"
NAMESPACE="yangbuyiya"
IMAGE_NAME="tencent-send-article-mcp-server-app"
IMAGE_TAG="1.0.0"

# Docker构建参数
BUILD_ARGS="--platform ${DOCKER_PLATFORM} --load --progress plain"

# 打印标题
print_title

# 检查 Docker 环境
print_step 1 6 "检查 Docker 环境..."
if ! command -v docker &> /dev/null; then
    print_error "Docker 未安装或未添加到环境变量中"
    print_error "请先安装 Docker 并确保添加到环境变量中"
    exit 1
fi
print_success "Docker 环境检查通过"
echo

# 检查 Docker 服务
print_step 2 6 "检查 Docker 服务..."
if ! docker info &> /dev/null; then
    print_error "Docker 服务未运行，请启动 Docker 服务"
    exit 1
fi
print_success "Docker 服务检查通过"
echo

# 检查配置文件
print_step 3 6 "检查配置文件..."
if [ ! -f "${SCRIPT_DIR}/.local-config" ]; then
    print_error "配置文件不存在"
    print_info "请创建 script/.local-config 文件并填写以下内容："
    echo -e "${WHITE}  ALIYUN_USERNAME=你的阿里云账号${NC}"
    echo -e "${WHITE}  ALIYUN_PASSWORD=你的阿里云密码${NC}"
    exit 1
fi
print_success "配置文件检查通过"
echo

# 读取认证信息
print_step 4 6 "读取认证信息..."
source "${SCRIPT_DIR}/.local-config"

if [ -z "$ALIYUN_USERNAME" ] || [ -z "$ALIYUN_PASSWORD" ]; then
    print_error "认证信息不完整"
    print_info "请确保 .local-config 文件中包含 ALIYUN_USERNAME 和 ALIYUN_PASSWORD"
    exit 1
fi
print_success "认证信息读取成功"
echo

# 构建 Docker 镜像
print_step 5 6 "开始构建 Docker 镜像..."
print_info "镜像名称: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
print_info "构建平台: ${DOCKER_PLATFORM}"

if [ ! -f "${DOCKERFILE_PATH}" ]; then
    print_error "Dockerfile不存在: ${DOCKERFILE_PATH}"
    print_info "当前目录: $(pwd)"
    exit 1
fi

if ! docker buildx build -f "${DOCKERFILE_PATH}" ${BUILD_ARGS} -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} .; then
    print_error "Docker构建失败！"
    exit 1
fi
print_success "构建完成"
echo

# 推送 Docker 镜像到阿里云
print_step 6 6 "推送 Docker 镜像到阿里云..."

# 登录到阿里云容器镜像服务
print_info "登录阿里云容器镜像服务..."
if ! docker login --username="${ALIYUN_USERNAME}" --password="${ALIYUN_PASSWORD}" ${ALIYUN_REGISTRY} > /dev/null 2>&1; then
    print_error "登录失败"
    print_info "请检查用户名和密码是否正确"
    exit 1
fi
print_success "登录成功"

# 为 Docker 镜像打标签
print_info "为 Docker 镜像打标签..."
if ! docker tag ${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG} ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG} > /dev/null 2>&1; then
    print_error "打标签失败"
    print_info "请检查本地镜像是否存在："
    echo -e "${WHITE}  docker images | grep ${IMAGE_NAME}${NC}"
    exit 1
fi
print_success "标签设置成功"

# 推送镜像
print_info "推送 Docker 镜像..."
if ! docker push ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}; then
    print_error "推送失败"
    print_info "请检查权限和网络连接"
    exit 1
fi
print_success "推送完成"

# 登出阿里云容器镜像服务
print_info "登出阿里云容器镜像服务..."
docker logout ${ALIYUN_REGISTRY} > /dev/null 2>&1
print_success "已安全登出"
echo

# 打印完成信息
print_separator
echo -e "${BLUE}║${NC} ${CYAN}                   部署完成！                    ${BLUE}║${NC}"
print_separator
echo

# 显示镜像信息
echo -e "${MAGENTA}镜像信息：${NC}"
echo -e "${WHITE}  仓库地址：${NC}${CYAN}${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}${NC}"
echo -e "${WHITE}  检出命令：${NC}${CYAN}docker pull ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}${NC}"
echo -e "${WHITE}  标签设置：${NC}${CYAN}docker tag ${ALIYUN_REGISTRY}/${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG} ${NAMESPACE}/${IMAGE_NAME}:${IMAGE_TAG}${NC}"
echo 