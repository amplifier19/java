FROM payara/server-full

COPY ./dummy-bookings.txt /opt

ARG war="BookingOptimization-1.0-SNAPSHOT.war"

COPY ./target/${war} ${DEPLOY_DIR}