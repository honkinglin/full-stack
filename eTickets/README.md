# ETicket

## dev

```
dotnet run
```

## Preset Role

- Admin  
```
admin@test.com  
Test123?  
```

- User  
```
user@test.com  
Test123?  
```

## Initialized
```
rm -rf ./app.db
dotnet ef migrations remove

dotnet ef migrations add InitialCreate
dotnet ef database update
```