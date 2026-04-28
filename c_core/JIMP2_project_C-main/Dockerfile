FROM ubuntu:24.04

RUN apt-get update && apt-get install -y \
    gcc \
    make \
    valgrind \
    python3 \
    python3-matplotlib \
    && rm -rf /var/lib/apt/lists/*

ENV MPLBACKEND=Agg

WORKDIR /app
COPY . .

RUN make

CMD ./bin/graph inp.txt out.txt fr text && python3 visualize.py
