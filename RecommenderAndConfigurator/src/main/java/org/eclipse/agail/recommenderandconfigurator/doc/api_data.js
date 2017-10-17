define({ "api": [
  {
    "type": "get",
    "url": "/getAppRecommendation",
    "title": "GetAppRecommendation",
    "name": "GetAppRecommendation",
    "version": "1.0.0",
    "group": "1_Recommender",
    "description": "<p>Returns Recommended Apps.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfApps",
            "description": "<p>List of Recommended Apps</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.href",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.stars",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.downloads",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n\n{\n    \"appList\": [\n        {\n            \"title\": \"App\",\n            \"href\": \"hebele\",\n            \"stars\": 0,\n            \"downloads\": 0\n        },\n        {\n            \"title\": \"mgarciap/iot-sensor-simulator/\",\n            \"href\": \"https://hub.docker.com/r/mgarciap/iot-sensor-simulator/\",\n            \"stars\": 0,\n            \"downloads\": 0\n        },\n        {\n            \"title\": \"cuongdd1/sensor-remote-dashboard/\",\n            \"href\": \"https://hub.docker.com/r/cuongdd1/sensor-remote-dashboard/\",\n            \"stars\": 0,\n            \"downloads\": 0\n        },\n        {\n            \"title\": \"sensorlab6/videk/\",\n            \"href\": \"https://hub.docker.com/r/sensorlab6/videk/\",\n            \"stars\": 0,\n            \"downloads\": 0\n        },\n       {\n            \"title\": \"elliotsabitov/sensorsservernode/\",\n            \"href\": \"https://hub.docker.com/r/elliotsabitov/sensorsservernode/\",\n            \"stars\": 0,\n            \"downloads\": 0\n        },\n        {\n            \"title\": \"elliotsabitov/sensorsclientreact/\",\n            \"href\": \"https://hub.docker.com/r/elliotsabitov/sensorsclientreact/\",\n            \"stars\": 0,\n           \"downloads\": 0\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "1_Recommender"
  },
  {
    "type": "get",
    "url": "/getCloudRecommendation",
    "title": "GetCloudRecommendation",
    "name": "GetCloudRecommendation",
    "version": "1.0.0",
    "group": "1_Recommender",
    "description": "<p>Returns Recommended Cloud Servers.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfClouds",
            "description": "<p>List of Recommended Clouds.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.link",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.accesstype",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.locations",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.middlewares",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.runtimes",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.services",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.pricing",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n\n{\n    \"cloudList\": [\n        {\n            \"title\": \"Cloud\",\n            \"link\": \"hebele4\",\n            \"accesstype\": \"\",\n            \"locations\": \"\",\n            \"middlewares\": \"\",\n            \"frameworks\": \"\",\n            \"runtimes\": \"\",\n            \"services\": \"\",\n            \"pricing\": \"\"\n        },\n        {\n            \"title\": \"Heroku \",\n            \"link\": \"https://www.heroku.com/ \",\n            \"accesstype\": \"public , private \",\n            \"locations\": \"EU , NA \",\n            \"middlewares\": \"m \",\n            \"frameworks\": \"django , flask , grails , play , rails \",\n            \"runtimes\": \"clojue , go , groovyjava , node , php , python , ruby , scala \",\n            \"services\": \"postgresql , redis , TODO_ADD_MORE \",\n            \"pricing\": \"metered , monthly , free \"\n        },\n        {\n            \"title\": \"OpenShift Online \",\n            \"link\": \"https://www.openshift.com/features/index.html \",\n            \"accesstype\": \"public , private \",\n            \"locations\": \"EU , NA \",\n            \"middlewares\": \"jboss , tomcat , zend server \",\n            \"frameworks\": \"django , drupal , flask , rails , switchyard , vert.x \",\n            \"runtimes\": \"java , node , perl , php , python , ruby \",\n            \"services\": \"jenkins , mongodb , mysql , openshift metrics , postgresql \",\n            \"pricing\": \"monthly , fixed , annually , free , hybrid \"\n        },\n        {\n            \"title\": \"Bluemix \",\n            \"link\": \"https://console.ng.bluemix.net/ \",\n            \"accesstype\": \"public , private \",\n            \"locations\": \"EU , NA , OC \",\n            \"middlewares\": \"m \",\n            \"frameworks\": \"rails , sinatra \",\n            \"runtimes\": \"go , java , node , php , python , ruby \",\n            \"services\": \"advancedd mobile access ,  alchemyapi ,  api management ,  application security manager ,  appscan dynamis analyzer ,  appscan mobile analyzer ,  TODO_ADD_MORE \",\n           \"pricing\": \"metered , monthly \"\n        },\n        {\n            \"title\": \"Microsoft Azure \",\n            \"link\": \"https://azure.microsoft.com/tr-tr/ \",\n            \"accesstype\": \"public \",\n            \"locations\": \"AS , EU , NA , OC , SA \",\n            \"middlewares\": \"tomcat \",\n            \"frameworks\": \"cakephp , django \",\n            \"runtimes\": \"dotnet , java , node , php , python , ruby \",\n            \"services\": \"TODO_ADD_MORE \",\n            \"pricing\": \"metered , monthly \"\n        },\n        {\n            \"title\": \"Atos Cloud Foundry \",\n            \"link\": \"https://canopy-cloud.com/application-platforms/atos-cloud-foundry \",\n            \"accesstype\": \"public , private \",\n            \"locations\": \"AS , EU , NA , OC , SA \",\n            \"middlewares\": \"jboss , tomcat , tomee \",\n            \"frameworks\": \"django , grails , hhvm , play , rack , rails , sinatra , spring \",\n            \"runtimes\": \"clojure , dotnet , go , groovy , java , node , php , python , ruby , scala , swift \",\n            \"services\": \"neo4j , abacus , cassandra , couchdb , dingo-postgresql , elasticsearch , kafka , memcached , mongodb , mysql , postgresql , rabbitmq , redis , riakcs ,  TODO_ADD_MORE \",\n            \"pricing\": \"metered , monthly , fixed \"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "1_Recommender"
  },
  {
    "type": "get",
    "url": "/getDeviceRecommendation",
    "title": "GetDeviceRecommendation",
    "name": "GetDeviceRecommendation",
    "version": "1.0.0",
    "group": "1_Recommender",
    "description": "<p>Returns Recommended Devices.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfDevices",
            "description": "<p>List of Recommended Devices.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.link",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n\n{\n    \"deviceList\": [\n        {\n            \"title\": \"Seeedstudio-Gas-Sensor-Socket\",\n            \"href\": \"https://www.amazon.com/Seeedstudio-Gas-Sensor-Socket/dp/B01C5RTCF4\"\n        },\n        {\n            \"title\": \"Seeedstudio-Grove-Gas-Sensor-MQ3\",\n            \"href\": \"https://www.amazon.com/Seeedstudio-Grove-Gas-Sensor-MQ3/dp/B01C5RNWW8\"\n        },\n        {\n            \"title\": \"Wavesahre-MQ-7-Semiconductor-Sensor-Gas\",\n            \"href\": \"https://www.amazon.com/Wavesahre-MQ-7-Semiconductor-Sensor-Gas/dp/B00NJNYWDG\"\n        },\n        {\n           \"title\": \"Wavesahre-MQ-2-Gas-Sensor-Detection\",\n            \"href\": \"https://www.amazon.com/Wavesahre-MQ-2-Gas-Sensor-Detection/dp/B00NJOIB50\"\n        },\n       {\n           \"title\": \"Waveshare-MQ-3-Gas-Sensor-Detection\",\n            \"href\": \"https://www.amazon.com/Waveshare-MQ-3-Gas-Sensor-Detection/dp/B00NL3KEYK\"\n        },\n        {\n            \"title\": \"MAUSAN-Temperature-Humidity-Sensor-Arduino\",\n            \"href\": \"https://www.amazon.com/MAUSAN-Temperature-Humidity-Sensor-Arduino/dp/B06XT4WWKW\"\n        },\n        {\n            \"title\": \"LM35-Temperature-Sensor-Component-pack\",\n            \"href\": \"https://www.amazon.com/LM35-Temperature-Sensor-Component-pack/dp/B01ISMVA1E\"\n        },\n        {\n            \"title\": \"WaveShare-Waveshare-DHT22-Temperature-Humidity-Sensor\",\n            \"href\": \"https://www.amazon.com/WaveShare-Waveshare-DHT22-Temperature-Humidity-Sensor/dp/B01C1CTW2G\"\n        },\n        {\n            \"title\": \"Venel-Has-Wavelength-760nm-1100nm-Light-Used-Fire-Fighting\",\n            \"href\": \"https://www.amazon.com/Venel-Has-Wavelength-760nm-1100nm-Light-Used-Fire-Fighting/dp/B01HI410AY\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "1_Recommender"
  },
  {
    "type": "get",
    "url": "/getWorkflowRecommendation",
    "title": "GetWorkflowRecommendation",
    "name": "GetWorkflowRecommendation",
    "version": "1.0.0",
    "group": "1_Recommender",
    "description": "<p>Returns Recommended Workflows.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfWFs",
            "description": "<p>List of Recommended Workflows.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.type",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.datatag",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.dataowner",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.href",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.description",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.installCommand",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.javascriptCode",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n\n{\n    \"wfList\": [\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,ads-b,dump1090,ibm\",\n            \"dataowner\": \"Neil Kolban\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-ads-b\",\n            \"description\": \"low-description\\\">A Node-Red ADS-B decoded node\",\n            \"installCommand\": \"npm install node-red-contrib-ads-b\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"pm,pm2.5,pm10,sensor,air,node-red\",\n            \"dataowner\": \"Jannik Becher\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-sds011-sensor\",\n            \"description\": \"low-description\\\">This is a Node Red node to manage connection to the SDS011 sensor on a Raspberry Pi. It allows you to specify the variables that define the connections to the sensor. This node is added to the Raspberry Pi section.\",\n            \"installCommand\": \"npm install node-red-contrib-sds011-sensor\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"pcap,Packet Capture,ARP,node-red\",\n            \"dataowner\": \"Nicholas Humfrey\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-pcap\",\n            \"description\": \"low-description\\\">Network packet capture for Node-RED\",\n            \"installCommand\": \"npm install node-red-contrib-pcap\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,lego,boost,move,hub,robotics,ble,bluetooth\",\n            \"dataowner\": \"Sebastian Raff\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-movehub\",\n            \"description\": \"low-description\\\">Node-RED Nodes to control the Lego Boost Move Hub\",\n            \"installCommand\": \"npm install node-red-contrib-movehub\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,aws-sdk\",\n            \"dataowner\": \"high-u\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-aws-sdk-anything\",\n            \"description\": \"low-description\\\">node-red aws sdk anything\",\n            \"installCommand\": \"npm install node-red-contrib-aws-sdk-anything\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,fritzbox,fritz,router,tr064,presence,avm,callmonitor,phonebook\",\n            \"dataowner\": \"Jochen Scheib\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-fritz\",\n            \"description\": \"low-description\\\">This node gives access to the fritzbox tr064 api\",\n            \"installCommand\": \"npm install node-red-contrib-fritz\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,Yamaha,AVR,RX-777,RX-677,RX-477,RX-A740,HTR-4065,TSR-5790\",\n            \"dataowner\": \"Sebastian Krauskopf\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-avr-yamaha\",\n            \"description\": \"low-description\\\">Node-RED node to connect to Yamaha Audio Video Receivers (e.g. Yamaha AVR RX-677)\",\n            \"installCommand\": \"npm install node-red-contrib-avr-yamaha\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"node\",\n            \"datatag\": \"node-red,db2,ibm,ibmi,os400,ibm i,iseries\",\n            \"dataowner\": \"Benoit Marolleau\",\n            \"href\": \"https://flows.nodered.org/node/node-red-contrib-db2-for-i\",\n            \"description\": \"low-description\\\">A Node-RED node to use a IBM DB2 for i database\",\n            \"installCommand\": \"npm install node-red-contrib-db2-for-i\",\n            \"javascriptCode\": null\n        },\n        {\n            \"type\": \"flow\",\n            \"datatag\": \"slack,bot,sdk\",\n            \"dataowner\": \"joshendriks\",\n            \"href\": \"https://flows.nodered.org/flow/51f68bd87a897caa5c3148457cc084c0\",\n            \"description\": \"e-red-contrib-slackbotsdk</h1>\\n          <p>This package implements a slackbot for <a href=\\\"https://slackapi.github.io/node-slack-sdk/\\\">node-red</a> using the official <a href=\\\"https://slackapi.github.io/node-slack-sdk/\\\">Slack Developer Kit for Node.js</a>\",\n            \"installCommand\": null,\n            \"javascriptCode\": \"[{\\\"id\\\":\\\"1d4829a6.6aa586\\\",\\\"type\\\":\\\"tab\\\",\\\"label\\\":\\\"Flow 1\\\",\\\"disabled\\\":false},{\\\"id\\\":\\\"baf3ec3e.50bc8\\\",\\\"type\\\":\\\"slackbot in\\\",\\\"z\\\":\\\"1d4829a6.6aa586\\\",\\\"name\\\":\\\"\\\",\\\"token\\\":\\\"a7e55e6.54507a\\\",\\\"x\\\":720,\\\"y\\\":120,\\\"wires\\\":[[\\\"bf5d7d2f.2fb\\\"]]},{\\\"id\\\":\\\"bf5d7d2f.2fb\\\",\\\"type\\\":\\\"change\\\",\\\"z\\\":\\\"1d4829a6.6aa586\\\",\\\"name\\\":\\\"Echo\\\",\\\"rules\\\":[{\\\"t\\\":\\\"set\\\",\\\"p\\\":\\\"payload\\\",\\\"pt\\\":\\\"msg\\\",\\\"to\\\":\\\"This is a test reply\\\",\\\"tot\\\":\\\"str\\\"}],\\\"action\\\":\\\"\\\",\\\"property\\\":\\\"\\\",\\\"from\\\":\\\"\\\",\\\"to\\\":\\\"\\\",\\\"reg\\\":false,\\\"x\\\":610,\\\"y\\\":380,\\\"wires\\\":[[\\\"baf3ec3e.50bc8\\\"]]},{\\\"id\\\":\\\"895ba886.b35758\\\",\\\"type\\\":\\\"inject\\\",\\\"z\\\":\\\"1d4829a6.6aa586\\\",\\\"name\\\":\\\"\\\",\\\"topic\\\":\\\"\\\",\\\"payload\\\":\\\"node-red slackbot is here\\\",\\\"payloadType\\\":\\\"str\\\",\\\"repeat\\\":\\\"\\\",\\\"crontab\\\":\\\"\\\",\\\"once\\\":false,\\\"x\\\":170,\\\"y\\\":139,\\\"wires\\\":[[\\\"fb623394.7c9ac\\\"]]},{\\\"id\\\":\\\"fb623394.7c9ac\\\",\\\"type\\\":\\\"change\\\",\\\"z\\\":\\\"1d4829a6.6aa586\\\",\\\"name\\\":\\\"\\\",\\\"rules\\\":[{\\\"t\\\":\\\"set\\\",\\\"p\\\":\\\"channel\\\",\\\"pt\\\":\\\"msg\\\",\\\"to\\\":\\\"channelID\\\",\\\"tot\\\":\\\"str\\\"}],\\\"action\\\":\\\"\\\",\\\"property\\\":\\\"\\\",\\\"from\\\":\\\"\\\",\\\"to\\\":\\\"\\\",\\\"reg\\\":false,\\\"x\\\":460,\\\"y\\\":100,\\\"wires\\\":[[\\\"baf3ec3e.50bc8\\\"]]},{\\\"id\\\":\\\"a7e55e6.54507a\\\",\\\"type\\\":\\\"slackbot-token\\\",\\\"z\\\":\\\"\\\",\\\"name\\\":\\\"Testbot\\\",\\\"token\\\":\\\"configure token here\\\"}]\"\n        },\n        {\n           \"type\": \"flow\",\n            \"datatag\": \"ibmi,iseries,db2\",\n            \"dataowner\": \"bmarolleau\",\n            \"href\": \"https://flows.nodered.org/flow/b255f32b8e07a5cc0c17e654fd338354\",\n            \"description\": \"e-red-contrib-db2-for-i  basic flow</h1>\\n          <h2 id=\\\"description\\\">Description</h2>\\n<p>A basic flow for reading or writing to a DB2 for i db using Node.js v6 / Node-RED on IBM i\",\n            \"installCommand\": null,\n            \"javascriptCode\": \"[{\\\"id\\\":\\\"f336e0ee.99fdf8\\\",\\\"type\\\":\\\"inject\\\",\\\"z\\\":\\\"e5e26fd6.5797\\\",\\\"name\\\":\\\"SQL Query\\\",\\\"topic\\\":\\\"database\\\",\\\"payload\\\":\\\"SELECT * FROM ACMEDB.MYTABLE\\\",\\\"payloadType\\\":\\\"str\\\",\\\"repeat\\\":\\\"\\\",\\\"crontab\\\":\\\"\\\",\\\"once\\\":false,\\\"x\\\":144,\\\"y\\\":470,\\\"wires\\\":[[\\\"8c1463e2.299be8\\\"]]},{\\\"id\\\":\\\"994e12fc.b6d598\\\",\\\"type\\\":\\\"debug\\\",\\\"z\\\":\\\"e5e26fd6.5797\\\",\\\"name\\\":\\\"\\\",\\\"active\\\":true,\\\"console\\\":\\\"false\\\",\\\"complete\\\":\\\"false\\\",\\\"x\\\":846,\\\"y\\\":472,\\\"wires\\\":[]},{\\\"id\\\":\\\"8c1463e2.299be8\\\",\\\"type\\\":\\\"template\\\",\\\"z\\\":\\\"e5e26fd6.5797\\\",\\\"name\\\":\\\"ACMEDB-Connection\\\",\\\"field\\\":\\\"database\\\",\\\"fieldType\\\":\\\"msg\\\",\\\"format\\\":\\\"handlebars\\\",\\\"syntax\\\":\\\"mustache\\\",\\\"template\\\":\\\"*LOCAL\\\",\\\"output\\\":\\\"str\\\",\\\"x\\\":390,\\\"y\\\":472,\\\"wires\\\":[[\\\"6800df26.7aed4\\\"]]},{\\\"id\\\":\\\"6800df26.7aed4\\\",\\\"type\\\":\\\"DB2 for i\\\",\\\"z\\\":\\\"e5e26fd6.5797\\\",\\\"mydb\\\":\\\"4d3a95ef.d7e97c\\\",\\\"name\\\":\\\"\\\",\\\"x\\\":603,\\\"y\\\":470,\\\"wires\\\":[[\\\"994e12fc.b6d598\\\"]]},{\\\"id\\\":\\\"4d3a95ef.d7e97c\\\",\\\"type\\\":\\\"DB2 for i Config\\\",\\\"z\\\":\\\"\\\",\\\"db\\\":\\\"*LOCAL\\\"}]\"\n        }\n    ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "1_Recommender"
  },
  {
    "type": "get",
    "url": "/updateRepositories",
    "title": "UpdateRepositories",
    "name": "UpdateRepositories",
    "version": "1.0.0",
    "group": "1_Recommender",
    "description": "<p>Updates the local repositories by getting new items from Amazon, Docker.hub and Node.Red websites.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "success",
            "description": "<p>is 0, error is -1</p>"
          }
        ]
      }
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "1_Recommender"
  },
  {
    "type": "get",
    "url": "/getResourceOptimization",
    "title": "GetResourceOptimization",
    "name": "GetResourceOptimization",
    "version": "1.0.0",
    "group": "2_Configurator",
    "description": "<p>Returns Resource Optimized Configuration.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number[]",
            "optional": false,
            "field": "supportedDataEncodingProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number[]",
            "optional": false,
            "field": "supportedConnectivityProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Performance",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Reliability",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Cost",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "installedApps",
            "description": "<p>Optimized Configuration of installed Apps.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "installedApps.name",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "installedApps.url",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_DataEncodingProtocol",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_ConnectivitiyProtocol",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "errormessage",
            "description": ""
          }
        ]
      }
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "2_Configurator"
  },
  {
    "type": "get",
    "url": "/getServiceConfiguration",
    "title": "GetServiceConfiguration",
    "name": "GetServiceConfiguration",
    "version": "1.0.0",
    "group": "3_Settings",
    "description": "<p>Returns the settings of the service.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object",
            "optional": false,
            "field": "staticServiceConfiguration",
            "description": "<p>Current settings of the service.</p>"
          },
          {
            "group": "Success 200",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForDevelopmentUIActive",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForDeviceManagementUIActive",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForAppManagementUIActive",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.allowRecommenderServerToUseGatewayProfile",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServerIP",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n\"recommenderServiceForDevelopmentUIActive\": false,\n\"recommenderServiceForDeviceManagementUIActive\": true,\n\"recommenderServiceForAppManagementUIActive\": true,\n\"allowRecommenderServerToUseGatewayProfile\": true,\n\"recommenderServerIP\": \"http://ec2-54-201-143-18.us-west-2.compute.amazonaws.com:8080/Recommender/\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "3_Settings"
  },
  {
    "type": "put",
    "url": "/setServiceConfiguration",
    "title": "SetServiceConfiguration",
    "name": "SetServiceConfiguration",
    "version": "1.0.0",
    "group": "3_Settings",
    "description": "<p>Updates the settings of the service.</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Object",
            "optional": false,
            "field": "staticServiceConfiguration",
            "description": "<p>Settings of the service.</p>"
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForDevelopmentUIActive",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForDeviceManagementUIActive",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServiceForAppManagementUIActive",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Boolean",
            "optional": false,
            "field": "staticServiceConfiguration.allowRecommenderServerToUseGatewayProfile",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "staticServiceConfiguration.recommenderServerIP",
            "description": ""
          }
        ]
      }
    },
    "success": {
      "fields": {
        "200": [
          {
            "group": "200",
            "optional": false,
            "field": "settings",
            "description": "<p>are updated successfully.</p>"
          }
        ]
      }
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "3_Settings"
  },
  {
    "type": "get",
    "url": "/getGatewayProfileForConfigurator",
    "title": "GetGatewayProfileForConfigurator",
    "name": "GetGatewayProfileForConfigurator",
    "version": "1.0.0",
    "group": "4_For_Testing_Purposes",
    "description": "<p>Returns the gateway profile used by Configurator.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Number[]",
            "optional": false,
            "field": "supportedDataEncodingProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number[]",
            "optional": false,
            "field": "supportedConnectivityProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Performance",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Reliability",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Cost",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "installedApps",
            "description": "<p>Optimized Configuration of installed Apps.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "installedApps.name",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "installedApps.url",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_DataEncodingProtocol",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_ConnectivitiyProtocol",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "errormessage",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "    HTTP/1.1 200 OK\n{\n    \"supportedDataEncodingProtocolsOfGateway\": [\n        0,\n        1,\n        2\n    ],\n    \"supportedConnectivityProtocolsOfGateway\": [\n        0,\n        3,\n        4\n    ],\n    \"userRequirementWeight_Performance\": 1,\n    \"userRequirementWeight_Reliability\": 1,\n    \"userRequirementWeight_Cost\": -1,\n    \"installedApps\": [\n        {\n            \"name\": \"fire alarm\",\n            \"url\": \"link1\",\n            \"inUse_DataEncodingProtocol\": 0,\n           \"inUse_ConnectivitiyProtocol\": 0,\n            \"supportedDataEncodingProtocolsOfApp\": [\n                0,\n               1\n           ],\n            \"supportedConnectivitiyProtocolsOfApp\": [\n                0,\n                2\n            ]\n        },\n        {\n            \"name\": \"temperature alarm\",\n            \"url\": \" link2\",\n            \"inUse_DataEncodingProtocol\": 0,\n            \"inUse_ConnectivitiyProtocol\": 1,\n            \"supportedDataEncodingProtocolsOfApp\": [\n               0\n            ],\n            \"supportedConnectivitiyProtocolsOfApp\": [\n                1\n            ]\n        },\n        {\n            \"name\": \"gas alarm\",\n            \"url\": \"link3\",\n            \"inUse_DataEncodingProtocol\": 0,\n            \"inUse_ConnectivitiyProtocol\": 0,\n            \"supportedDataEncodingProtocolsOfApp\": [\n                0,\n                2\n            ],\n            \"supportedConnectivitiyProtocolsOfApp\": [\n                0\n            ]\n        }\n    ],\n    \"errorMessage\": null\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "4_For_Testing_Purposes"
  },
  {
    "type": "get",
    "url": "/getGatewayProfileForRecommender",
    "title": "GetGatewayProfileForRecommender",
    "name": "GetGatewayProfileForRecommender",
    "version": "1.0.0",
    "group": "4_For_Testing_Purposes",
    "description": "<p>Returns the gateway profile used by Recommender.</p>",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfDevices",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.link",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfApps",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.href",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.stars",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.downloads",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfWFs",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.type",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.datatag",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.dataowner",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.href",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfClouds",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.title",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.link",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.accesstype",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.locations",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.middlewares",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.runtimes",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.services",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.pricing",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "location",
            "description": ""
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "pricingPreferences",
            "description": ""
          }
        ]
      },
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n\n{\n    \"devices\": {\n       \"deviceList\": [\n            {\n                \"title\": \"camera\",\n                \"href\": \"link4\"\n            },\n            {\n                \"title\": \"gas sensor\",\n                \"href\": \"link5\"\n            },\n            {\n                \"title\": \"lcd\",\n                \"href\": \"link6\"\n            }\n        ]\n    },\n    \"apps\": {\n        \"appList\": [\n            {\n                \"title\": \"fire alarm\",\n                \"href\": \"link1\",\n                \"stars\": 0,\n                \"downloads\": 0\n            },\n            {\n                \"title\": \"temperature alarm\",\n                \"href\": \"link2\",\n                \"stars\": 0,\n                \"downloads\": 0\n            },\n            {\n                \"title\": \"gas alarm\",\n                \"href\": \"link3\",\n                \"stars\": 0,\n                \"downloads\": 0\n            }\n        ]\n    },\n    \"wfs\": {\n        \"wfList\": [\n            {\n                \"type\": \"node\",\n               \"datatag\": \"datatag1\",\n                \"dataowner\": \"datawner1\",\n                \"href\": \"link7\"\n            },\n            {\n                \"type\": \"workflow\",\n                \"datatag\": \"datatag2\",\n                \"dataowner\": \"datawner2\",\n                \"href\": \"link8\"\n            },\n            {\n                \"type\": \"workflow\",\n                \"datatag\": \"datatag3\",\n                \"dataowner\": \"datawner3\",\n                \"href\": \"link9\"\n            }\n        ]\n    },\n    \"clouds\": {\n        \"cloudList\": [\n           {\n                \"title\": \"cloud1\",\n                \"link\": \"link10\",\n                \"accesstype\": null,\n                \"locations\": null,\n                \"middlewares\": null,\n                \"frameworks\": null,\n                \"runtimes\": null,\n                \"services\": null,\n                \"pricing\": null\n            },\n            {\n                \"title\": \"cloud2\",\n                \"link\": \"link11\",\n                \"accesstype\": null,\n                \"locations\": null,\n                \"middlewares\": null,\n                \"frameworks\": null,\n                \"runtimes\": null,\n                \"services\": null,\n                \"pricing\": null\n            }\n        ]\n   },\n    \"location\": \"EU\",\n    \"pricingPreferences\": \"free , metered\"\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "4_For_Testing_Purposes"
  },
  {
    "type": "put",
    "url": "/setGatewayProfileForRecommender",
    "title": "SetGatewayProfileForRecommender",
    "name": "SetGatewayProfileForRecommender",
    "version": "1.0.0",
    "group": "4_For_Testing_Purposes",
    "description": "<p>Sets the gateway profile used by Recommender.</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfDevices",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.title",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfDevices.link",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfApps",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.title",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfApps.href",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.stars",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "ListOfApps.downloads",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfWFs",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.type",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.datatag",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.dataowner",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfWFs.href",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "ListOfClouds",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.title",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.link",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.accesstype",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.locations",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.middlewares",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.runtimes",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.services",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "ListOfClouds.pricing",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "location",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "pricingPreferences",
            "description": ""
          }
        ]
      }
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "4_For_Testing_Purposes"
  },
  {
    "type": "put",
    "url": "/setGatewayProfileForConfigurator",
    "title": "SetGatewayProfileForConfigurator",
    "name": "setGatewayProfileForConfigurator",
    "version": "1.0.0",
    "group": "4_For_Testing_Purposes",
    "description": "<p>Sets the gateway profile used by Configurator.</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "Number[]",
            "optional": false,
            "field": "supportedDataEncodingProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number[]",
            "optional": false,
            "field": "supportedConnectivityProtocolsOfGateway",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Performance",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Reliability",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "userRequirementWeight_Cost",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Object[]",
            "optional": false,
            "field": "installedApps",
            "description": "<p>Optimized Configuration of installed Apps.</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "installedApps.name",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "installedApps.url",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_DataEncodingProtocol",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "Number",
            "optional": false,
            "field": "installedApps.inUse_ConnectivitiyProtocol",
            "description": ""
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "errormessage",
            "description": ""
          }
        ]
      }
    },
    "filename": "./RnCAPI.java",
    "groupTitle": "4_For_Testing_Purposes"
  },
  {
    "description": "<p>AGILE Recommender and Configurator Docker Service</p>",
    "version": "1.0.0",
    "type": "",
    "url": "",
    "filename": "./RnCAPI.java",
    "group": "C__Users_spolater_Desktop_AGILE_AGILE_GITHUB_Configurator_Configurator_RecommenderAndConfigurator_src_main_java_org_eclipse_agail_recommenderandconfigurator_RnCAPI_java",
    "groupTitle": "C__Users_spolater_Desktop_AGILE_AGILE_GITHUB_Configurator_Configurator_RecommenderAndConfigurator_src_main_java_org_eclipse_agail_recommenderandconfigurator_RnCAPI_java",
    "name": ""
  }
] });
