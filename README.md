# Matryoshka

![Matryoshka](matryoshka.jpg)

Navigate easily through a java Map (Matryoshka) with a kind of path. Avoiding yourself to write many times get() and validate if it is not null to avoid NullPointerExceptions. A kind of super limited XPath.

Imagine a map (expressed in JSON format)

```json
{
	"id": 1,
	"title": "Casino Royale",
	"director": "Martin Campbell",
	"starring": 
	[
		{
			"name" : "Daniel",
			"surname" : "Craig",
			"country" : {
				"name" : "United kingdom",
				"short" : "uk"
			}
			
		},
		{
			"name" : "Eva",
			"surname" : "Green",
			"country" : {
				"name" : "France",
				"short" : "fr"
			}
			
		}
	],
	"desc": "Twenty-first James Bond movie",
	"release date": 1163466000000,
	"box office": 594275385
}
```

1 = matryoshka.get("id").value(); 
"Twenty-first James Bond movie" = matryoshka.get("desc").value();
"Daniel" = matryoshka.get("starring").asList().get(0).get("name");
"uk" = matryoshka.get("starring").asList().get(0).get("country/short");

or storing partial results to improve performance

Matryoshka danielCraig = matryoshka.get("starring").asList().get(0);
"Daniel"= danielCraig.get("name")
"uk"= danielCraig.get("country/short")
"United Kingdom"= danielCraig.get("country/name")


## API

- get(String): To retrieve data from the map structure using map keys splitted with '/'. ex: get("/root/object/value") or get("root","object","value")

Once you are in a node you can retrieve data with:

- value(): You get 'value' of this node.
- asMatryoshka(): You get anther Matryoshka from this node. Only return null if it is a leaf without value. 
- asList(): You get a list of Matryoshkas form this node. Never return null but empty list.
