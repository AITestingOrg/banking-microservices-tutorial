FROM ruby:2.4.5-alpine3.9
MAINTAINER justin_phillips@ultimatesoftware.com

# Install dependencies for Ruby native extensions
RUN apk update
RUN apk upgrade
RUN apk add --virtual build_deps build-base sqlite sqlite-dev sqlite-libs sqlite-doc
# Install Git
RUN apk add --no-cache bash git openssh
# Get source
RUN git clone https://github.com/pact-foundation/pact_broker.git
WORKDIR /pact_broker/example
# Install dependencies
RUN bundle install --without='development test'
# Setup Environment vars
ENV TZ Eastern Time (US & Canada)
ENV RACK_ENV "development"

EXPOSE 9292
# Run!
CMD bundle exec rackup
