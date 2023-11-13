FROM node:16.16.0

ARG WEB_WORK_DIR

WORKDIR ${WEB_WORK_DIR}

COPY . .

RUN npm install --force

CMD [ "npm", "run", "start" ]
