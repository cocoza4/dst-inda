    // console.log($('#report'));

    var width = 900,
        height = 500;

    var color = d3.scale.category20();

    var force = d3.layout.force()
        .charge(-120)
        .linkDistance(30)
        .size([width, height]);

    var svg = d3.select("#report").append("svg")
        .attr("width", width)
        .attr("height", height);

    // d3.json("miserables.json", function(error, graph) {
    d3.json("planningFolder.json", function(error, graph) {
        if (error) throw error;

        // create temp index as tindex
        _.each(graph.nodes, function(e, index) {
            e.tindex = index;
        });

        // count
        var countBy = _.countBy(graph.nodes, function(e) {
            return e.category;
        });
        console.log(countBy);

        // find color code
        var keys = _.keys(countBy);
        _.each(keys, function(key, index) {
            console.log(color(key));
        });

        // console.log(graph.nodes);
        // filter server nodes
        // var server = _.filter(graph.nodes, function(e) {
        //     return e.category == "server";
        // });
        // console.log(server);
        // console.log(_.size(server));

        // create links {}
        // var links = {};
        // for (i = 0; i < graph.nodes.length; i++) {
        //     if (graph.nodes[i].category == "server") {
        //         links[i] = {
        //             source: 0,
        //             target: i
        //         };
        //     }
        //     if (graph.nodes[i].category == "new portal") {
        //         links[i] = {
        //             source: 5,
        //             target: i
        //         };
        //     }
        //     if (graph.nodes[i].category == "communications") {
        //         links[i] = {
        //             source: 2,
        //             target: i
        //         };
        //     }
        // }
        // console.log(links);
        // // transform d3 model
        // links = d3.values(links);

        force
            .nodes(graph.nodes)
            .links(graph.links)
            // .links(graph.links)
            // .links(links)
            .start();

        var link = svg.selectAll(".link")
            .data(graph.links)
            .enter().append("line")
            .attr("class", "link");
        // .style("stroke-width", function(d) { return Math.sqrt(d.value); });

        var node = svg.selectAll(".node")
            .data(graph.nodes)
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag)
            .on('dblclick', connectedNodes); //Added code for highlighting;

        node.append("circle")
            .attr("r", 8)
            .style("fill", function(d) {
                return color(d.category)
            });

        // node.append("text")
        //     .attr("dx", 10)
        //     .attr("dy", ".35em")
        //     .text(function(d) {
        //         return d.category
        //     })
        //     .style("stroke", "gray");

        node.append("title")
            .text(function(d) {
                return d.artifactId;
            });


        force.on("tick", function(e) {
            link.attr("x1", function(d) {
                    return d.source.x;
                })
                .attr("y1", function(d) {
                    return d.source.y;
                })
                .attr("x2", function(d) {
                    return d.target.x;
                })
                .attr("y2", function(d) {
                    return d.target.y;
                });

            //Changed to display label

            d3.selectAll("circle").attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                });

            d3.selectAll("text").attr("x", function(d) {
                    return d.x;
                })
                .attr("y", function(d) {
                    return d.y;
                });

            //End Changed

        });

        //---Insert--for highlight-----

        //Toggle stores whether the highlighting is on
        var toggle = 0;





        //Create an array logging what is connected to what
        var linkedByIndex = {};
        for (i = 0; i < graph.nodes.length; i++) {
            linkedByIndex[i + "," + i] = 1;
        };

        graph.links.forEach(function(d) {
            linkedByIndex[d.source.index + "," + d.target.index] = 1;
        });
        //This function looks up whether a pair are neighbours  
        function neighboring(a, b) {
            return linkedByIndex[a.index + "," + b.index];
        }

        function connectedNodes() {

            if (toggle == 0) {
                //Reduce the opacity of all but the neighbouring nodes
                d = d3.select(this).node().__data__;
                node.style("opacity", function(o) {
                    return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1;
                });

                link.style("opacity", function(o) {
                    return d.index == o.source.index | d.index == o.target.index ? 1 : 0.1;
                });

                //Reduce the op

                toggle = 1;
            } else {
                //Put them back to opacity=1
                node.style("opacity", 1);
                link.style("opacity", 1);
                toggle = 0;
            }

        }

        function createLinks(nodes) {
            for (i = 0; i < graph.nodes.length; i++) {
                linkedByIndex[i + "," + i] = 1;
            };
        }
    });
