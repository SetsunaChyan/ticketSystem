<template>
    <div id="app">
        <div id="nav">
            <el-menu class="el-menu-demo" mode="horizontal">
                <el-menu-item index="1"><router-link to="/">购票中心</router-link></el-menu-item>
                <el-menu-item index="2"><router-link to="/cart">购到的票</router-link></el-menu-item>
            </el-menu>
        </div>
        <router-view/>
    </div>
</template>

<style>
    #app {
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
    }

    #nav {
        text-align: center;
    }

    #nav {
        padding: 0px;
    }

    #nav a {
        font-weight: bold;
        color: #2c3e50;
        text-decoration:none;
    }

    #nav a.router-link-exact-active {
        color: #42b983;
    }
</style>
