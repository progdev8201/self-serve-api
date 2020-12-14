docker system prune -a -f;
docker build --tag momothebest/backend_repo .;
docker push momothebest/backend_repo;