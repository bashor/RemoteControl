import tornado.websocket
import tornado.web
from multiprocessing import Pool
from logging import debug

class PipeHandler(tornado.websocket.WebSocketHandler):
	"""Handles input WebSocket events, like new client connection, message receiving
	   and connection closing.
	"""
	__process_pool = Pool()
	__connections = {}
	def open(self):
		"""Handles new client connections. Stores new client id and self in a dict.
		   Another connection with this client closes, if any.
		"""
		self.target_id = self.get_argument("to")
		self.source_id = self.get_argument("from")
		if (self.source_id in self.__class__.__connections):
			debug("duplicate connection",
						extra={
								"target" : self.__class__.__connections[self.source_id].target_id,
								"source" : self.source_id
						}
			)
			self.__class__.__connections[self.source_id].close()
		self.__class__.__connections[self.source_id] = self
		debug( "new connection accepted", extra={"target" : self.target_id, "source" : self.source_id} )

	@tornado.web.asynchronous	
	def on_message(self, message):
		"""Handles input messages. Messages retransmits to target without any
		   modifications (operates like a pipe). Target connections takes from a dict.
		"""
		debug( "message received: %s", message, extra={"target" : self.target_id, "source" : self.source_id} )
		if (self.target_id in self.__class__.__connections):
			target_conn = self.__class__.__connections[self.target_id]
			if (target_conn.target_id == self.source_id):
				debug("message retransmition")
				self.__class__.__process_pool.apply_async( target_conn.write_message, (message,) )

	def on_close(self):
		"""Handles connection closing. Removes connection id from a dict."""
		debug( "close connection", extra={"target" : self.target_id, "source" : self.source_id} )
		del self.__class__.__connections[self.source_id]
