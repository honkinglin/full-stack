```bash
# resets the database by dropping all data and tables, reapplying all migrations, and optionally seeding the database.
npx prisma migrate reset

# creates and applies a new migration with the specified name "init" based on the current Prisma schema, and updates the development database accordingly.
npx prisma migrate dev --name init

# opens Prisma Studio, a web-based GUI for browsing and managing the data in your database, allowing you to view and edit records in an easy-to-use interface.
npx prisma studio
```
