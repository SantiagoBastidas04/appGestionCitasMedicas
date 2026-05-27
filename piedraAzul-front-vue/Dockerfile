# ── Etapa 1: build ──────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# Variables de build
ARG VITE_KEYCLOAK_URL
ARG VITE_KEYCLOAK_REALM
ARG VITE_KEYCLOAK_CLIENT
ARG VITE_KEYCLOAK_SECRET
ARG VITE_API_URL

ENV VITE_KEYCLOAK_URL=$VITE_KEYCLOAK_URL
ENV VITE_KEYCLOAK_REALM=$VITE_KEYCLOAK_REALM
ENV VITE_KEYCLOAK_CLIENT=$VITE_KEYCLOAK_CLIENT
ENV VITE_KEYCLOAK_SECRET=$VITE_KEYCLOAK_SECRET
ENV VITE_API_URL=$VITE_API_URL

RUN npm run build

# ── Etapa 2: servir con nginx ────────────────────────────────
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
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]