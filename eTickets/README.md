# ETicket

## dev

```
dotnet run
```

## Preset Role

- Admin  
```
admin@etickets.com  
Coding@1234?  
```

- User  
```
user@etickets.com  
Coding@1234?  
```

## Initialized
```
dotnet ef migrations remove
rm -rf ./app.db

dotnet ef migrations add InitialCreate
dotnet ef database update
```