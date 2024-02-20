# Frontend

## Details

- VueJS library was used for the development of the frontend. Instead of installing VueJS via npm it was useed from a CDN. This option made things light and easier, enhancing the static html code.

- No additional npm modules was used.

## Deployment

- After the successful deployment of the Java API, change the `apiURL` address in the data section of the `index.js` file. The value should match the url address of the java API.  

- In your local environment, run a web server of your choice (i.e. Live Server), in order to create an endpoint for your browser.

*Dont hesitate to containarize the frontend part of the application as well (i.e. nginx container).*