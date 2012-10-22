import sys

if __name__ == "__main__":
	sys.path.append("dynamic")
	sys.path.append("static")
	sys.path.append("common")
	sys.path.append(".")
	import server
	server.run()
