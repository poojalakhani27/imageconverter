# RGB Image generator
A Spring boot application to convert GeoTiff single channel images to RGB image.

## Design approach 
1. Since image generation is a very heavy process, it is optimised to respond with a pre-generated image. Two design approaches were considered here :
    1. **Scheduled generation of images** - This approach is only efficient when the request for generated images are sparse enough that fetches most of the images. Otherwise, it is an overkill.
    1. **Generate image on 1st request and serve the generated image on subsequent requests** - This approach was adopted because as mentioned in point 1, eagerly generating all the images is a premature optimisation if we do not know if the requests are sparse enough that fetches most of the images. There will be latency only in the first request and subsequent requests will be fetched from the cache.
1. **StorageEngine** is abstracted to wire any implementation like LocalStorageEngine, GCSStorageEngine etc.
1. Due to the JDK bug in TIFFImageReader.read(), https://bugs.openjdk.java.net/browse/JDK-8145019 , java version should be higher than 8. I used java 11.
1. **Project Lombok** is used for making data classes concise.

## Future improvements
1. **Caching headers in response** can be sent for client side caching. This will reduce the server load.
1. **ETag** (digest) implementation for each generated image can done so that **HTTP 304 Not Modified** status can be sent, rather than streaming image every time.

## Assumptions
1. All the TIFF images are of same height and width, so that pixel calculation can take any image’s width and height.

## Running the application:
1. Provide the directory of Granules data in application.properties for property *single.channel.file.directory*. Make sure it is prefixed with “file:” and ends with a slash. E.g. single.channel.file.directory=file:../sample-granule/ 
1. mvn spring-boot:run
1. curl -XPOST -H "Content-Type: application/json" --data '{"utmZone": 33, "latitudeBand": "U", "gridSquare": "UP", "date":"2018-08-04", "channelMap": "visible"}' http://localhost:8081/generate-images >> ./generated.jpg
