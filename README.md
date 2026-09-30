# CampusMarket —— 校园二手交易平台

本科简历级全栈项目：需求分析 → 数据库设计 → 前后端开发 → Docker 一键部署。

## 技术栈
- 用户端：Vue3 + TypeScript + UniApp（一套代码编译微信小程序/H5/App）
- 管理端：Vue3 + Element Plus（单文件 HTML）
- 后端：Java 17 + Spring Boot 3 + MyBatis-Plus + JWT + WebSocket
- 数据库：MySQL 8
- 中间件：Redis / RabbitMQ / Elasticsearch（docker-compose 一键起）
- 部署：Docker + Docker Compose + Nginx

## 已实现功能
- 用户注册/登录（JWT + BCrypt）
- 商品发布/列表/详情/搜索/分类
- 图片上传
- 收藏/取消收藏
- 订单闭环：下单 → 支付 → 确认收货 → 取消
- WebSocket 买卖家实时聊天
- 管理后台：数据看板 / 用户管理 / 商品审核 / 订单查看
- 管理员权限拦截（role=1）

## 本地开发
1. 后端：cd campus-market-server，先建库 campus_market 执行 deploy/init.sql，再 mvn spring-boot:run
2. 前端：HBuilderX 打开 campus-market-uniapp 运行到浏览器
3. 管理后台：双击 campus-market-admin/index.html（管理员 test001 / 123456）

## Docker 一键部署
cd deploy && docker compose up -d
