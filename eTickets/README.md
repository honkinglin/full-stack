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
rm -rf ./app.db
dotnet ef migrations remove

dotnet ef migrations add InitialCreate
dotnet ef database update
```