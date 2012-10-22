import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web

import os

from tornado.options import define, options
define("port", default=80, help="run server on given port", type=int)

from indexhandler import IndexHandler

def run():
	tornado.options.parse_command_line()
	app = tornado.web.Application(
		handlers=[(r"/", IndexHandler)],
		template_path=os.path.join(os.path.dirname(__file__), "templates")
	)
	http_server = tornado.httpserver.HTTPServer(app)
	http_server.listen(options.port)
	tornado.ioloop.IOLoop.instance().start()
