package com.at.spark;

import static spark.Spark.connect;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.head;
import static spark.Spark.options;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.trace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

public class SparkRestfulServer {
	private final static Logger logger = LoggerFactory.getLogger(SparkRestfulServer.class);

	public static class RestResInner {
		private String val;

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}
	}

	public static class RestRes {
		private String status;
		private RestResInner content;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public RestResInner getContent() {
			return content;
		}

		public void setContent(RestResInner content) {
			this.content = content;
		}
	}

	public static void main(String[] args) {
		logger.info("Enterring main.");
		Gson gson = new Gson();
		get("/hello/:name", new Route() {
			public Object handle(Request req, Response res) {
				String x_auth_token = req.headers("X-Auth-Token");
				res.status(202);
				res.type("application/json");
				res.header("X-Auth-Token", x_auth_token);

				RestResInner restResInner = new RestResInner();
				restResInner.setVal("Hello " + req.params(":name") + ", get");
				RestRes restRes = new RestRes();
				restRes.setStatus("202");
				restRes.setContent(restResInner);
				return restResInner; // "{ \"text\" : \"Hello
										// "+req.params(":name")+", get\" }" ;
			}
		}, gson::toJson);
		post("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", post");
		put("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", put");
		patch("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", patch");
		delete("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", delete");
		options("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", options");
		// headers only
		head("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", head");
		//
		trace("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", trace");
		//
		connect("/hello/:name", (req, res) -> "Hello " + req.params(":name") + ", connect");

		logger.info("Exiting main.");
	}
}
