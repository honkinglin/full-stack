# Mall Project

### Technology stack

- Spring Boot 3.x
- Mybatis 3.x
- Mysql 9.x
- Redis 3.x
- Vue 3.x

### Usage

```bash
# build project
mvn clean package -DskipTests

# start project
docker-compose up -d
```

### Development
```bash
# build frontend
cd frontend/vue3-mall/
pnpm install
pnpm build

# build admin frontend
cd frontend/vue3-mall-admin/
pnpm install
pnpm build
```

[!map](./docs/markmap.svg)
