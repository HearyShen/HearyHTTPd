import os

print("HTTP/1.0 200 OK")
print("Content-type: text/html")
print()
print("<b>Environment Variables</b>")

print("<ul>")
for key, value in os.environ.items():
    print(f"<li>{key} = {value}</li>")
print("</ul>")