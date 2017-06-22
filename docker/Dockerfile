FROM debian:9
MAINTAINER Patrick Kreutzer <patrick.kreutzer@fau.de>

ENV DEBIAN_FRONTEND noninteractive

RUN apt-get update -q && apt-get install -qy \
  locales \
  git \
  bsdmainutils \
  openjdk-8-jdk \
  zsh && \
  rm -rf /var/lib/apt/lists/*

RUN useradd -m user -s /bin/bash

RUN echo "en_US.UTF-8 UTF-8\n" > /etc/locale.gen
RUN locale-gen

ADD util/run_ares.sh /home/user/
RUN chown user:user /home/user/run_ares.sh && chmod +x /home/user/run_ares.sh

USER user

RUN git clone https://github.com/FAU-Inf2/ARES.git /home/user/repository
RUN cd /home/user/repository && ./gradlew build

WORKDIR /home/user
ENTRYPOINT ["/home/user/run_ares.sh"]
