import tornado.websocket
import tornado.web

import logging

class PipeHandler(tornado.websocket.WebSocketHandler):
    """Handles input WebSocket events, like new client connection, message
       receiving and connection closing.
    """
    __connections = {}
    __logger = logging.getLogger(__name__)

    def open(self):
        """Handles new client connections. Stores new client id and self in a
           dict. Another connection with this client closes, if any.
        """
        self.target_id = self.get_argument("to")
        self.source_id = self.get_argument("from")
        if self.source_id in self.connections():
            self.logger().debug("duplicate connection",
                extra={
                    "target": self.connections()[self.source_id].target_id,
                    "source": self.source_id
                }
            )
            self.connections()[self.source_id].close()
        self.connections()[self.source_id] = self
        self.logger().debug("new connection accepted",
            extra={
                "target": self.target_id,
                "source": self.source_id
            }
        )

    def on_message(self, message):
        """Handles input messages. Messages retransmits to target without any
           modifications (operates like a pipe). Target connections takes from
           a dict.
        """
        self.logger().debug("message received: %s", message,
            extra={
                "target": self.target_id,
                "source": self.source_id
            }
        )
        if self.target_id in self.connections():
            target_conn = self.connections()[self.target_id]
            if (target_conn.target_id == self.source_id):
                self.logger().debug("message retransmition")
                target_conn.write_message(message)

    def on_close(self):
        """Handles connection closing. Removes connection id from a dict."""
        self.logger().debug("close connection",
            extra={
                "target": self.target_id,
                "source": self.source_id
            }
        )
        del self.connections()[self.source_id]

    def connections(self):
        return self.__class__.__connections

    def logger(self):
        return self.__class__.__logger
