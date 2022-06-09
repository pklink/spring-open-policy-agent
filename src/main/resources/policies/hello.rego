package hello

default allow := false

allow {
    input.method == "GET"
    input.path == "/hello"
    input.user == "bernie"
}