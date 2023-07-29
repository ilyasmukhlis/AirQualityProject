# General documentation on the use of the architecture

##### Content
[Structure of modules in the project](#structModule)  
[Package structure in the project](#structPackage) <br>
[How to work with a dark theme?](#darkTheme)<br>

<a name="structModule"><h2>Structure of modules in the project</h2></a>

<h4>Currently Project contains only single and main module - app</h4>

<a name="structPackage"><h2>Package structure in the project</h2></a>
<h4>The package structure should be the same in all modules</h4>
   <p><b>1. data</b></p>
   <p><b>2. domain</b></p>
   <p><b>3. ui</b></p>
   <p><b>4. utils</b></p>
   <p><b>5. di</b></p>

<h4>What does the data package contain</h4>
   <p><b>data</b>- data-layer should contain should contain all the constant values<br>
   </p>
  <h4>What does the domain package contain</h4>
   <p><b>domain</b>- domain-layer contains all of the use cases and requests to the APIs<br>
   </p>
   <h4>What does the ui package contain</h4>
    <p><b>ui</b>- ui-layer contains all of the user interaction screen and ui logic<br>
    </p>
    <h4>What does the utils package contain</h4>
    <p><b>utils</b>- utils should contain utilities and extension methods
    </p>
   <h4>What does the di package contain</h4>
    <p><b>di</b>- di-layer is necessary for building dependency injection across the application</p>

<a name="darkTheme"><h2>How to work with a dark theme?</h2></a>
<p>In order for the dark theme to work throughout the application, it is necessary to prescribe colors in both files (color and color-night), also the name of the colors must be the same, the colors that are used in other modules must be in the core module (when created),
if the color is needed only in 1 module, then we prescribe it in the current module.</p>

<h4>Note: In order to successfully pass the review and keep the architecture, you need to follow all the rules described above!</h4>
