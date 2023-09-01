"use strict";(self.webpackChunkangular_16_jwt_auth=self.webpackChunkangular_16_jwt_auth||[]).push([[127],{7127:(P,c,s)=>{s.r(c),s.d(c,{PagesLayoutModule:()=>M});var a=s(4755),u=s(7738),t=s(3020),d=s(3144);const m="http://localhost:8080/api/test/";let g=(()=>{class e{constructor(o){this.http=o}getPublicContent(){return this.http.get(m+"all",{responseType:"text"})}getUserBoard(){return this.http.get(m+"user",{responseType:"text"})}getModeratorBoard(){return this.http.get(m+"mod",{responseType:"text"})}getAdminBoard(){return this.http.get(m+"admin",{responseType:"text"})}}return e.\u0275fac=function(o){return new(o||e)(t.LFG(d.eN))},e.\u0275prov=t.Yz7({token:e,factory:e.\u0275fac,providedIn:"root"}),e})(),f=(()=>{class e{constructor(o){this.userService=o}ngOnInit(){this.userService.getAdminBoard().subscribe({next:o=>{this.content=o},error:o=>{if(o.error)try{const r=JSON.parse(o.error);this.content=r.message}catch{this.content=`Error with status: ${o.status} - ${o.statusText}`}else this.content=`Error with status: ${o.status}`}})}}return e.\u0275fac=function(o){return new(o||e)(t.Y36(g))},e.\u0275cmp=t.Xpm({type:e,selectors:[["app-board-admin"]],decls:4,vars:1,consts:[[1,"container"],[1,"jumbotron"]],template:function(o,r){1&o&&(t.TgZ(0,"div",0)(1,"header",1)(2,"p"),t._uU(3),t.qZA()()()),2&o&&(t.xp6(3),t.Oqu(r.content))}}),e})();var i=s(4315);function h(e,n){if(1&e&&t._UZ(0,"map-marker",4),2&e){const o=n.$implicit,r=t.oxw();t.Q6J("position",o)("options",r.markerOptions)}}let _=(()=>{class e{constructor(){this.center={lat:24,lng:12},this.zoom=4,this.markerOptions={draggable:!1},this.markerPositions=[]}ngOnInit(){}addMarker(o){null!=o.latLng&&this.markerPositions.push(o.latLng.toJSON())}}return e.\u0275fac=function(o){return new(o||e)},e.\u0275cmp=t.Xpm({type:e,selectors:[["app-board-moderator"]],decls:6,vars:3,consts:[[1,"container"],[1,"jumbotron"],["height","600px","width","100%",3,"center","zoom","mapClick"],[3,"position","options",4,"ngFor","ngForOf"],[3,"position","options"]],template:function(o,r){1&o&&(t.TgZ(0,"div",0)(1,"header",1)(2,"h1"),t._uU(3,"Driver"),t.qZA()(),t.TgZ(4,"google-map",2),t.NdJ("mapClick",function(p){return r.addMarker(p)}),t.YNc(5,h,1,2,"map-marker",3),t.qZA()()),2&o&&(t.xp6(4),t.Q6J("center",r.center)("zoom",r.zoom),t.xp6(1),t.Q6J("ngForOf",r.markerPositions))},dependencies:[a.sg,i.b6,i.O_]}),e})();function C(e,n){if(1&e&&t._UZ(0,"map-marker",4),2&e){const o=n.$implicit,r=t.oxw();t.Q6J("position",o)("options",r.markerOptions)}}let v=(()=>{class e{constructor(){this.markers=[],this.center={lat:5.797013518730928,lng:-.11960143593987116},this.zoom=6,this.markerOptions={draggable:!1}}ngOnInit(){}moveMap(o){null!=o.latLng&&(this.center=o.latLng.toJSON(),this.markers.push(o.latLng.toJSON()),console.log("lat: "+o.latLng.toJSON().lat+", lng: "+o.latLng.toJSON().lng))}move(o){null!=o.latLng&&(this.display=o.latLng.toJSON())}addMarker(o){}}return e.\u0275fac=function(o){return new(o||e)},e.\u0275cmp=t.Xpm({type:e,selectors:[["app-board-user"]],decls:10,vars:5,consts:[[1,"container"],[1,"jumbotron"],["height","600px","width","100%",3,"center","zoom","mapClick","mapMousemove"],[3,"position","options",4,"ngFor","ngForOf"],[3,"position","options"]],template:function(o,r){1&o&&(t.TgZ(0,"div",0)(1,"header",1)(2,"h1"),t._uU(3,"Customer"),t.qZA()(),t.TgZ(4,"google-map",2),t.NdJ("mapClick",function(p){return r.moveMap(p)})("mapMousemove",function(p){return r.move(p)}),t.YNc(5,C,1,2,"map-marker",3),t.qZA(),t.TgZ(6,"div"),t._uU(7),t.qZA(),t.TgZ(8,"div"),t._uU(9),t.qZA()()),2&o&&(t.xp6(4),t.Q6J("center",r.center)("zoom",r.zoom),t.xp6(1),t.Q6J("ngForOf",r.markers),t.xp6(2),t.hij("Latitude: ",null==r.display?null:r.display.lat,""),t.xp6(2),t.hij("Longitude: ",null==r.display?null:r.display.lng,""))},dependencies:[a.sg,i.b6,i.O_]}),e})(),U=(()=>{class e{constructor(o){this.userService=o}ngOnInit(){}}return e.\u0275fac=function(o){return new(o||e)(t.Y36(g))},e.\u0275cmp=t.Xpm({type:e,selectors:[["app-home"]],decls:4,vars:1,consts:[[1,"container"],[1,"jumbotron"]],template:function(o,r){1&o&&(t.TgZ(0,"div",0)(1,"header",1)(2,"p"),t._uU(3),t.qZA()()()),2&o&&(t.xp6(3),t.Oqu(r.content))}}),e})();var T=s(3897);function y(e,n){if(1&e&&(t.TgZ(0,"li"),t._uU(1),t.qZA()),2&e){const o=n.$implicit;t.xp6(1),t.hij(" ",o," ")}}function O(e,n){if(1&e&&(t.TgZ(0,"div",2)(1,"header",3)(2,"h3")(3,"strong"),t._uU(4),t.qZA(),t._uU(5," Profile "),t.qZA()(),t.TgZ(6,"p")(7,"strong"),t._uU(8,"Email:"),t.qZA(),t._uU(9),t.qZA(),t.TgZ(10,"strong"),t._uU(11,"Roles:"),t.qZA(),t.TgZ(12,"ul"),t.YNc(13,y,2,1,"li",4),t.qZA()()),2&e){const o=t.oxw();t.xp6(4),t.Oqu(o.currentUser.username),t.xp6(5),t.hij(" ",o.currentUser.email," "),t.xp6(4),t.Q6J("ngForOf",o.currentUser.roles)}}function Z(e,n){1&e&&t._uU(0," Please login.\n")}const A=[{path:"home",component:U},{path:"profile",component:(()=>{class e{constructor(o){this.storageService=o}ngOnInit(){this.currentUser=this.storageService.getUser()}}return e.\u0275fac=function(o){return new(o||e)(t.Y36(T.V))},e.\u0275cmp=t.Xpm({type:e,selectors:[["app-profile"]],decls:3,vars:2,consts:[["class","container",4,"ngIf","ngIfElse"],["loggedOut",""],[1,"container"],[1,"jumbotron"],[4,"ngFor","ngForOf"]],template:function(o,r){if(1&o&&(t.YNc(0,O,14,3,"div",0),t.YNc(1,Z,1,0,"ng-template",null,1,t.W1O)),2&o){const l=t.MAs(2);t.Q6J("ngIf",r.currentUser)("ngIfElse",l)}},dependencies:[a.sg,a.O5]}),e})()},{path:"user",component:v},{path:"mod",component:_},{path:"admin",component:f}];let B=(()=>{class e{}return e.\u0275fac=function(o){return new(o||e)},e.\u0275mod=t.oAB({type:e}),e.\u0275inj=t.cJS({imports:[u.Bz.forChild(A),u.Bz]}),e})();var k=s(5030);let M=(()=>{class e{}return e.\u0275fac=function(o){return new(o||e)},e.\u0275mod=t.oAB({type:e}),e.\u0275inj=t.cJS({imports:[a.ez,B,k.u5,d.JF,i.Y4]}),e})()}}]);