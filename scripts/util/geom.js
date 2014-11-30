function ArcPathMaker(){

	this.norm = function(a,b){
		return Math.sqrt( Math.pow(a[0] - b[0],2) + Math.pow(a[1] - b[1],2));
	}

	this.radius = function(ab,offset){
		return offset == 0 ? 0 : 0.5*offset + ab*ab/(8*offset);
	}

	this.center = function(a,b,r){
		if( a[0] != b[0] ){
			var u = (a[0]*a[0] - b[0]*b[0] + a[1]*a[1] - b[1]*b[1])/(2*(a[0]-b[0]));
			var v = (a[1] - b[1])/(a[0] - b[0]);

			var alp = v*v +1;
			var bet = 2*(v*b[0] - u*v -b[1]);
			var gam = u*u - 2*u*b[0] + b[0]*b[0] + b[1]*b[1] - r*r;
			var delta = Math.max(bet*bet - 4*alp*gam,0);

			//console.log("a "+alp+", b "+bet+" c "+gam+", delta "+delta);

			var y = (Math.sqrt(delta) - bet)/(2*alp);

			var x = u - y*v;
			return [x,y];
		} else if ( a[1] != b[1] ) {
			// todo : refactor, plenty of things are the same
			var u = (a[0]*a[0] - b[0]*b[0] + a[1]*a[1] - b[1]*b[1])/(2*(a[1]-b[1]));
			var v = (a[0] - b[0])/(a[1] - b[1]);

			var alp = v*v +1;
			var bet = 2*(v*b[1] - u*v -b[0]);
			var gam = u*u - 2*u*b[1] + b[0]*b[0] + b[1]*b[1] - r*r;
			var delta = Math.max(bet*bet - 4*alp*gam,0);

			//console.log("a "+alp+", b "+bet+" c "+gam+", delta "+delta);

			var x = (Math.sqrt(delta) - bet)/(2*alp);

			var y = u - x*v;
			return [x,y];
		} else{
			return null;
		}
	}

	this.scal = function(a,b){
		return a[0]*b[0] + a[1]*b[1];
	}

	this.maxAngle = function(ca,cb,radius){
		var cos = this.scal(ca,cb)/(radius*radius);
		return Math.acos(cos);
	}

	this.vectSeg = function(a,b){
		return [b[0]-a[0],b[1]-a[1]];
	}

	this.rotatedPoint = function(c,ca,alp) {
		var cos = Math.cos(alp), sin = Math.sin(alp);
		return [c[0] + ca[0]*cos - ca[1]*sin,
				c[1] + ca[0]*sin + ca[1]*cos];
	};

	this.createPath = function(a,b,c,r,lineCount) {
		var ca = this.vectSeg(c,a);
		var cb = this.vectSeg(c,b);
		var maxAngle = this.maxAngle(ca,cb,r);
		var alp = maxAngle/lineCount;
		var result = [];
		result[0] = a;
		console.log(a,b,c,ca,cb,maxAngle,alp);
		for( var i = 1; i < lineCount; ++i){
			result[i] = this.rotatedPoint(c,ca,alp*i);
		}
		result[result.length] = b;
		return result;
	};

	this.arcPath = function(a,b,offsetPer,lineCount){
		var norm = this.norm(a,b);
		// maximum gap between the line ab and the arc
		var d = offsetPer*norm;
		if(d == 0) return [a,b];
		// radius of the arc
		var r = this.radius(norm,d);
		var c = this.center(a,b,r);
		var cview = L.circle(c, r);
		return this.createPath(a,b,c,r,lineCount);
	}
};
var ArcMaker = new ArcPathMaker();