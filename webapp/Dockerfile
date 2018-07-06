FROM nginx:stable

# Install curl
RUN apt-get update && apt-get install -y \
    curl \
 && rm -rf /var/lib/apt/lists/*
# Remove default config
RUN rm /etc/nginx/conf.d/*
# Copy custom config
COPY default.conf /etc/nginx/conf.d/default.conf
# Remove default website
RUN rm -rf /usr/share/nginx/html/*
# Copy website artifacts
ARG DIST_DIR=target
COPY ${DIST_DIR} /usr/share/nginx/html
# Make sure nginx has all permissions
RUN chown -R nginx:nginx /usr/share/nginx
RUN chmod -R u+rwx /usr/share/nginx
HEALTHCHECK --interval=30s --timeout=30s --retries=12 CMD curl -f http://localhost:8081 || exit 1
