FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html

RUN printf 'server {\n\
    listen 80;\n\
    root /usr/share/nginx/html;\n\
    index index.html;\n\
    location / {\n\
        try_files $uri $uri/ /index.html;\n\
    }\n\
}\n' > /etc/nginx/conf.d/default.conf

EXPOSE 8080

CMD ["sh", "-c", "sed -i 's/listen 80;/listen 8080;/g' /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]