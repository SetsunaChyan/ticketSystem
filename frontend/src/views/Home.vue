<template>
    <div class="home">
        <br>
        <FlightTable/>
    </div>
</template>

<script>
    // @ is an alias to /src
    import FlightTable from '@/components/FlightTable.vue'

    export default {
        name: 'Home',
        components: {
            FlightTable
        }
    }
</script>
