# https://hub.docker.com/_/microsoft-dotnet
FROM mcr.microsoft.com/dotnet/sdk:5.0-alpine-amd64 AS build
WORKDIR /app
# copy csproj and restore as distinct layers
COPY *.csproj /app
RUN dotnet restore

# copy everything else and build app
COPY . /app
RUN dotnet publish -c release -o /appbin --no-restore

# final stage/image
FROM mcr.microsoft.com/dotnet/aspnet:5.0-alpine-amd64
WORKDIR /app
COPY --from=build /appbin .
ENTRYPOINT ["dotnet", "app.dll"]
