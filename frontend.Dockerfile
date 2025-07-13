FROM node:16.3.0-alpine3.13

COPY dist dist
COPY node_modules node_modules
COPY package.json .
COPY package-lock.json .

USER 1001:1001

ENTRYPOINT npm run server
